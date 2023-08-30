
const SIGNICAT_STATE = "signicatRequestState";

export const Signicat = {
  authorizeUrl: window.__RUNTIME_CONFIG__.SIGNICAT_AUTHORIZE_URL,
  responseType: "code",
  scope: "openid profile signicat.national_id",
  clientId: window.__RUNTIME_CONFIG__.SIGNICAT_CLIENT_ID,
  redirectUri: window.__RUNTIME_CONFIG__.SIGNICAT_REDIRECT_URI,
  acrValues: window.__RUNTIME_CONFIG__.SIGNICAT_ACR_VALUES,
  profile: window.__RUNTIME_CONFIG__.SIGNICAT_PROFILE,

  url: (state: string) => {
    const url = new URL(Signicat.authorizeUrl);
    url.searchParams.append("response_type", Signicat.responseType);
    url.searchParams.append("scope", Signicat.scope);
    url.searchParams.append("signicat_profile", Signicat.profile);
    url.searchParams.append("client_id", Signicat.clientId);
    url.searchParams.append("redirect_uri", Signicat.redirectUri);
    url.searchParams.append("acr_values", Signicat.acrValues);
    url.searchParams.append("state", state);
    return url.toString();
  },

  rememberState: (state: string) => {
    sessionStorage.setItem(SIGNICAT_STATE, state);
  },

  handleRedirect: () : Promise<string> => {
    const params = new URLSearchParams(window.location.search);
    const state = params.get("state");
    if (!state || !Signicat.isStateValid(state)) {
      return Promise.reject("Invalid or missing state");
    }

    const code = params.get("code");
    if (!code) {
      return Promise.reject("Missing code");
    }
    return Promise.resolve(code);
  },

  isStateValid: (stateFromUrl: string) => {
    const decoded = window.atob(stateFromUrl);
    return decoded === sessionStorage.getItem(SIGNICAT_STATE);
  },

  clearState: () => {
    sessionStorage.removeItem(SIGNICAT_STATE);
  }

}
