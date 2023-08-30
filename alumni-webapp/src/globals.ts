export {};

declare global {
  interface Window {
    __RUNTIME_CONFIG__: {
      BASE_PATH: string;
      HELPDESK_PATH: string;
      MSAL_CLIENT_ID: string;
      MSAL_PRIVATE_AUTHORITY: string
      MSAL_CORPORATE_AUTHORITY: string;
      MSAL_REDIRECT_URI: string;
      MSAL_KNOWN_AUTHORITIES: string
      SIGNICAT_AUTHORIZE_URL: string
      SIGNICAT_CLIENT_ID: string
      SIGNICAT_REDIRECT_URI: string
      SIGNICAT_ACR_VALUES: string
      SIGNICAT_PROFILE: string
      INSIGHTS_CONNECTION_STRING: string
    };
  }
}
