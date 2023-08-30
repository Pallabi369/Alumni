import {loginRequest, msalConfig, tokenRequest} from "../authConfig";
import {AxiosRequestConfig, AxiosResponse} from 'axios';
import {QueryClient} from "react-query";
import {AuthError, EventMessage, EventType, PublicClientApplication} from "@azure/msal-browser";
import {corporateId, hasCorporateId} from "./url";
import {appStore} from "../store/appStore";
import {ReactPlugin} from "@microsoft/applicationinsights-react-js";
import {ApplicationInsights} from "@microsoft/applicationinsights-web";
import {SeverityLevel} from "@microsoft/applicationinsights-common";
import log from "loglevel";
import {getClaimSsid, getClaimZalarisId} from "./claims";

// QUERY CLIENT
export const queryClient = new QueryClient();

// MSAL INSTANCE
const config = {...msalConfig};
config.auth.authority = hasCorporateId() ?
  window.__RUNTIME_CONFIG__.MSAL_CORPORATE_AUTHORITY : window.__RUNTIME_CONFIG__.MSAL_PRIVATE_AUTHORITY;
export const msal = new PublicClientApplication(config);
msal.addEventCallback((message: EventMessage) => {

  switch (message.eventType) {
    // @ts-ignore
    case EventType.LOGIN_FAILURE:
      if (message.error?.message.startsWith('access_denied: AADB2C90091')) {
        break;
      }
    case EventType.LOGOUT_FAILURE:
    case EventType.ACQUIRE_TOKEN_BY_CODE_FAILURE:
    case EventType.SSO_SILENT_FAILURE:
    case EventType.ACQUIRE_TOKEN_FAILURE:
      appInsights.trackException({
        exception: message.error!, severityLevel: SeverityLevel.Error
      }, { ...collectUserData(), [AREA_PROPERTY]: "msal"});
      log.error(message.error);
      break;
    case EventType.LOGOUT_SUCCESS:
      appStore().clear();
      break;
  }
});

// APP INSIGHTS
export const reactPlugin = new ReactPlugin();
export const appInsights = new ApplicationInsights({
  config: {
    // even it makes no sense (there is a connectionString, right?) do not throw away this empty instrumentationKey
    // not to break 'application insights' sdk
    instrumentationKey: '',
    connectionString: window.__RUNTIME_CONFIG__.INSIGHTS_CONNECTION_STRING,
    enableAutoRouteTracking: true,
    extensions: [reactPlugin]
  }
});
appInsights.loadAppInsights();
if (window.__RUNTIME_CONFIG__.INSIGHTS_CONNECTION_STRING) {
  appInsights.addTelemetryInitializer(item => {
    item.tags?.push({'ai.cloud.role': 'alumni-webapp'});
  });
}  else {
  // deactivate appInsights
  appInsights.addTelemetryInitializer(() => false);
}

// LOGGING AREA
export const AREA_PROPERTY = "origin";

// HELPERS
export function withAccessToken<T>(operation: (config: AxiosRequestConfig) => Promise<AxiosResponse<T, any>>) {
  return () => msal.acquireTokenSilent(tokenRequest)
    .then(response => {
      return operation({ headers: {"Authorization" : `Bearer ${response.accessToken}`}})
    })
    .catch(error => {
      if (error instanceof AuthError) {
        if (appStore().corporateId) {
          const cid = corporateId();
          msal.loginRedirect(loginRequest(cid)).catch(error => {
            appInsights.trackException({
              exception: error, severityLevel: SeverityLevel.Error
            }, { ...collectUserData(), [AREA_PROPERTY]: "msal"});
            log.error(error);
          });
        } else {
          // noinspection JSIgnoredPromiseFromCall
          msal.logoutRedirect();
        }
      }
      throw error;
    });
}

export function collectUserData() {
  return {
    ssid: getClaimSsid(),
    zalarisId: getClaimZalarisId(),
    selectedCompany: appStore().employment?.companyCode
  }
}

export function cacheableQueryKey(prefix: string) {
  const { employment: { zalarisId} = {}} = appStore()
  if (!zalarisId) {
    appInsights.trackTrace({
        message: "No employment selected", severityLevel: SeverityLevel.Critical },
      { ...collectUserData(), [AREA_PROPERTY]: "services"});
    log.error("No employment selected");
    throw new Error("No employment selected");
  }
  return prefix + "-" + zalarisId;
}
