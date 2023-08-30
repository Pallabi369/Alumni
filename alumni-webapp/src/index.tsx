import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import './i18n';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {MsalProvider} from "@azure/msal-react";
import {appInsights, AREA_PROPERTY, collectUserData, msal} from "./services/services";
import OnboardingStateProvider from "./onboarding/OnboardingStateProvider";
import ErrorPageFallback from "./components/ErrorPageFallback";
import {ErrorBoundary} from "react-error-boundary";
import {SeverityLevel} from "@microsoft/applicationinsights-common";
import log, {LogLevelDesc} from "loglevel";

const searchParams = new URLSearchParams(window.location.search);
const debugLevel = searchParams.get('alumnidebug');
if (debugLevel) {
  try {
    log.setDefaultLevel(debugLevel as LogLevelDesc);
  } catch (ignored) {}
}

ReactDOM.render(
  <React.StrictMode>
    <OnboardingStateProvider>
      <MsalProvider instance={msal}>
          <ErrorBoundary
            onError={error => {
              appInsights.trackException({
                error: error,
                exception: error,
                severityLevel: SeverityLevel.Error,
              }, { ...collectUserData(), [AREA_PROPERTY]: "errorBoundary"});
            }}
            fallbackRender={({error}) => (<ErrorPageFallback error={error}/>)}
          >
            <App />
          </ErrorBoundary>
      </MsalProvider>
    </OnboardingStateProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
