import {actions, createMachine, DoneInvokeEvent, send} from "xstate";

import {MSAL_READY} from "./events";
import {appInsights, AREA_PROPERTY, collectUserData, msal, queryClient} from "../services/services";
import {sendCode} from "../services/token";
import {loginRequest} from "../authConfig";
import {appStore} from "../store/appStore";
import {getEmploymentHistory} from "../services/employees";
import {corporateId, hasCorporateId, isRedirect, navigateToBasePath} from "../services/url";
import {Signicat} from "../signicat";
import {getClaimCorporateId, getClaimSsid, getClaimZalarisId, verifyZalarisClaim} from "../services/claims";
import log from "loglevel";
import {SeverityLevel} from "@microsoft/applicationinsights-common";

const STATE_MACHINE = "stateMachine";

const { pure } = actions;

const UNEXPECTED_ERROR = "UNEXPECTED_ERROR";

// Initialization internal events
const OFFICE_SIGN_IN = "OFFICE_SIGN_IN";
const HOME_SIGN_IN = "HOME_SIGN_IN";
const SIGNED_IN_WITH_INVALID_TOKEN = "SIGNED_IN_WITH_INVALID_TOKEN";
const SIGNED_IN = "SIGNED_IN";

// Verification internal events
const VERIFICATION_REQUIRED = "VERIFICATION_REQUIRED";
const ASSIGN_NATIONAL_ID = "ASSIGN_NATIONAL_ID";
const ASSIGNMENT_VERIFICATION = "ASSIGNMENT_VERIFICATION";
const VERIFICATION_PASSED = "VERIFICATION_PASSED";

/**
 * sessionStorage key to be set while waiting for 'ssid' claim update
 */
const WAITING_FOR_SSID_CLAIM_KEY = "waiting_for_ssid_claim_key";

export const stateMachine = createMachine({
  id: "onboarding-state-machine",
  initial: "starting_up",

  on: {
    // global error handler
    [UNEXPECTED_ERROR]: 'unexpected_error'
  },

  states: {
    unexpected_error: {
      description: 'critical error',
      entry: () => {
        appInsights.trackTrace({
            message: "entered unexpected_error state", severityLevel: SeverityLevel.Information },
          { ...collectUserData(), [AREA_PROPERTY]: STATE_MACHINE});
        log.info(STATE_MACHINE + ": entered unexpected_error state");
      },
      type: "final"
    },
    starting_up: {
      description: 'waiting for signal from MSAL to full initialization',
      /**
       * Initial step required to synchronize with the msal lifecycle.
       * Hint: msal.handleRedirectPromise() ...
       */
      entry: () => {
        log.info(STATE_MACHINE + ": entered starting_up state");
      },
      on: {
        [MSAL_READY]: {
          target: "initialization",
          actions: safeActions(() => {
            if (!appStore().corporateId) {
              const corporateId = getClaimCorporateId();
              if (corporateId) {
                appStore().setCorporateId(corporateId);
              }
            }
          })
        }
      }
    },
    initialization: {
      description: 'checking accessToken and requested policy',
      entry: initializationActions(),
      on: {
        [OFFICE_SIGN_IN]: "office_signin",
        [HOME_SIGN_IN]: "home_signin",
        [SIGNED_IN_WITH_INVALID_TOKEN]: "signed_with_invalid_token",
        [SIGNED_IN]: "national_id_verification",
      }
    },
    office_signin: {
      description: "automatic login",
      entry: () => {
        log.info(STATE_MACHINE + ": entered office_signin state");
        const cid = corporateId();
        appStore().setCorporateId(cid);
        // It is NOT recommended having code that is dependent on the resolution of the Promise.
        // noinspection JSIgnoredPromiseFromCall
        msal.loginRedirect(loginRequest(cid)).catch(error => {
          appInsights.trackException({
            exception: error, severityLevel: SeverityLevel.Error
          }, { ...collectUserData(), [AREA_PROPERTY]: "msal"});
          log.error(error);
          throw error;
        });
      },
      type: "final"
    },
    home_signin: {
      description: "signin in at the user's request",
      entry: () => {
        log.info(STATE_MACHINE + ": entered home_signin state");
        appStore().setCorporateId("");
      },
      /**
       * Expecting user to click on home signin
       */
      type: "final",
    },
    signed_with_invalid_token: {
      description: "cannot proceed with an invalid token",
      entry: () => {
        const user = appStore().corporateId ?
          getClaimZalarisId() : getClaimSsid()
        appInsights.trackEvent({
          name: 'User ' + user + ' provided the wrong token for the required scenario ' + window.location.href,
        }, { ...collectUserData(), [AREA_PROPERTY]: STATE_MACHINE});
        log.info(STATE_MACHINE + ": entered signed_with_invalid_token state");
      },
      type: "final"
    },
    national_id_verification: {
      description: "national id verification loop",
      entry: verificationActions(),
      on: {
        [VERIFICATION_REQUIRED]: "verification_required",
        [VERIFICATION_PASSED]: "loading",
        [ASSIGN_NATIONAL_ID]: "assign_national_id",
        [ASSIGNMENT_VERIFICATION]: "national_id_assignment_verification"
      }
    },
    verification_required: {
      description: "verification is required",
      entry: () => {
        log.info(STATE_MACHINE + ": entered verification_required state");
      },
      /**
       * Expecting user to click on 'verification' link
       */
      type: "final",
    },
    assign_national_id: {
      description: "assigning national id",
      entry: () => {
        log.info(STATE_MACHINE + ": entered assign_national id state");
      },
      /**
       * Signicat verification finished. Ready to send code to the mapper service and update national id.
       */
      invoke: {
        src: () => {
          return Signicat.handleRedirect()
            .then(code => {
              return sendCode(code)
                .then(_ => {
                  Signicat.clearState();
                  sessionStorage.setItem(WAITING_FOR_SSID_CLAIM_KEY, "true");
                  navigateToBasePath();
                })
            })
        },
        onError: "assign_national_id_failed",
        onDone: "national_id_assignment_verification"
      }
    },
    assign_national_id_failed: {
      description: "assigning national_id failed",
      entry: () => {
        log.info(STATE_MACHINE + ": entered assign_national_id_failed state");
      },
      type: "final"
    },
    national_id_assignment_verification: {
      description:
        "waiting for B2C to update national id",
      entry: () => {
        log.info(STATE_MACHINE + ": entered national_id_assignment_verification state");
      },
      /**
       * A user is updated with the ssid claim. It's time to get it.
       */
      after: {
        3000: {
          actions: () => msal.loginRedirect(loginRequest())
        }
      }
    },
    loading: {
      description: "everything set up and verified, loading data",
      entry: () => {
        log.info(STATE_MACHINE + ": entered loading state");
        sessionStorage.removeItem(WAITING_FOR_SSID_CLAIM_KEY);
        if (!!getClaimSsid()) {
          appStore().setSsid(getClaimSsid());
        }
      },
      invoke: {
        src: () => {
          return Promise.resolve().then(_ => {
            const zalarisId = getClaimZalarisId() || undefined;
            return queryClient.fetchQuery('employmentHistory', getEmploymentHistory(zalarisId),
              {retry: 1, staleTime: Infinity})
              .then(employmentsHistory => {
                // always set the default employment to avoid unambiguity when user starts
                // the application bypassing dashboard ...
                const {employment, setEmployment} = appStore();
                if (employment === undefined) {
                  const {data: {employments = []} = {}} = employmentsHistory || {};
                  // filter out sync in progress/failed and take first one
                  const defaultEmployment = employments.find(e => !!e.companyName);
                  if (!defaultEmployment) {
                    return Promise.reject({response: {status: 404}});
                  }
                  setEmployment(defaultEmployment);
                }
              })
          });
        },
        onError: [
          {
            target: "onboarded_nodata",
            cond: (_, event) => isErrorStatus404(event) === true
          },
          {
            target: "onboarded_service_unavailable",
            cond: (_, event) => isErrorStatus404(event) === false
          },
          {
            target: "unexpected_error",
            cond: (_, event) => isErrorStatus404(event) === undefined
          }
        ],
        onDone: "onboarded"
      }
    },
    onboarded_nodata: {
      description: "no data is available",
      entry: () => {
        log.info(STATE_MACHINE + ": entered onboarded_nodata state");
      },
      type: "final"
    },
    onboarded_service_unavailable: {
      description: "the service is down or responds with an error",
      entry: () => {
        log.info(STATE_MACHINE + ": entered onboarded_service_unavailable state");
      },

      type: "final"
    },
    onboarded: {
      description: "onboarding finished",
      entry: () => {
        log.info(STATE_MACHINE + ": entered onboarded state");
      },
      type: "final"
    }
  },
});

function initializationActions() {
  return pure(() => {
    try {
      log.info(STATE_MACHINE + ": entered initialization state");
      switch (isOfficeSignIn()) {
        case 'yes':
          return send(OFFICE_SIGN_IN);
        case 'no':
          return send(HOME_SIGN_IN);
        case 'n/a':
          return send(verifyZalarisClaim() ?
            SIGNED_IN : SIGNED_IN_WITH_INVALID_TOKEN);
      }
    } catch (error: Error | any) {
      appInsights.trackException({
        exception: error, severityLevel: SeverityLevel.Error
      }, { ...collectUserData(), [AREA_PROPERTY]: STATE_MACHINE});
      log.error(error);
      return send(UNEXPECTED_ERROR);
    }
  });
}

function isOfficeSignIn() : "yes" | "no" | "n/a" {
  if (isAuthenticated()) return "n/a";
  return hasCorporateId() ? "yes" : "no";
}

function isAuthenticated() {
  return !!msal.getActiveAccount();
}

function verificationActions() {
  return pure(() => {
    try {
      log.info(STATE_MACHINE + ": entered verification state");
      if (!!appStore().corporateId || !!getClaimSsid()) {
        return send(VERIFICATION_PASSED);
      } else {
        if (isRedirect()) {
          return send(ASSIGN_NATIONAL_ID);
        } else if (!!sessionStorage.getItem(WAITING_FOR_SSID_CLAIM_KEY)) {
          return send(ASSIGNMENT_VERIFICATION);
        }
        return send(VERIFICATION_REQUIRED);
      }

    } catch (error: Error | any) {
      appInsights.trackException({
        exception: error, severityLevel: SeverityLevel.Error },
        { ...collectUserData(), [AREA_PROPERTY]: STATE_MACHINE});
      log.error(error);
      return send(UNEXPECTED_ERROR);
    }
  });
}

function isErrorStatus404(event: DoneInvokeEvent<any>): boolean | undefined {
  const status = getErrorStatus(event);
  if (status !== undefined) {
    return status === 404;
  }
}

function getErrorStatus(event: DoneInvokeEvent<any>) : number | undefined {
  const { data: { response: { status = undefined } = {} } = {} } = event;
  return status && status as number;
}

function safeActions(fn: () => void) {
  return pure(() => {
    try {
      fn();
    } catch (error: Error | any) {
      appInsights.trackException({
          exception: error, severityLevel: SeverityLevel.Error },
        { ...collectUserData(), [AREA_PROPERTY]: STATE_MACHINE});
      log.error(error);
      return send(UNEXPECTED_ERROR)
    }
  })
}

export default stateMachine;
