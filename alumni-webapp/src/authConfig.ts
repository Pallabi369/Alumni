import {Configuration} from "@azure/msal-browser/dist/config/Configuration";
import {LogLevel} from "@azure/msal-common";
import {RedirectRequest} from "@azure/msal-browser/dist/request/RedirectRequest";

export const msalConfig : Configuration = {
  auth: {
    clientId: window.__RUNTIME_CONFIG__.MSAL_CLIENT_ID,
    redirectUri: window.__RUNTIME_CONFIG__.MSAL_REDIRECT_URI,
    postLogoutRedirectUri: window.__RUNTIME_CONFIG__.BASE_PATH,
    knownAuthorities: [window.__RUNTIME_CONFIG__.MSAL_KNOWN_AUTHORITIES]
  },
  cache: {
    cacheLocation: "sessionStorage",
    storeAuthStateInCookie: process.env.NODE_ENV === 'production',
    secureCookies: process.env.NODE_ENV === 'production',
  },
  system: {
    loggerOptions: {
      logLevel: LogLevel.Verbose,
      piiLoggingEnabled: process.env.NODE_ENV !== 'production'
    }
  }
};

export const loginRequest = (corporateId: string | null = null) : RedirectRequest => {
  const loginRequest : RedirectRequest = {
    scopes: ["openid", "profile", window.__RUNTIME_CONFIG__.MSAL_CLIENT_ID],
  };

  if (corporateId) {
    loginRequest.extraQueryParameters = {
      domain_hint: corporateId
    }
  }
  return loginRequest;
};

export const tokenRequest = {
  scopes: [window.__RUNTIME_CONFIG__.MSAL_CLIENT_ID]
}
