import React, {Suspense, useContext, useEffect, useState} from "react";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import './App.css';
import DashboardPage from "./pages/DashboardPage";
import Layout from "./Layout";
import {QueryClientProvider} from "react-query";
import PageLoader from "./components/PageLoader";
import {PersonalDataPage} from "./pages/PersonalDataPage";
import {useActor} from "@xstate/react";
import OnboardingPage from "./onboarding/OnboardingPage";
import {OnboardingStateContext} from "./onboarding/OnboardingStateProvider";
import {msal, queryClient} from "./services/services";
import {MSAL_READY} from "./onboarding/events";
import {PayrollDataPage} from "./pages/PayrollDataPage";
import {useAppStore} from "./store/appStore";
import {PayrollDetailsView} from "./payrolldata/PayrollDetailsView";
import {PayrollResultsView} from "./payrolldata/PayrollResultsView";
import {PAYROLL_DATA_PAGE, PERSONAL_DATA_PAGE} from "./routes";
import {AuthError} from "@azure/msal-browser";

export default function App() {
  const globalServices = useContext(OnboardingStateContext);
  const [state] = useActor(globalServices.machineService);
  const [, setError] = useState();

  useEffect(() => {
    msal.handleRedirectPromise()
      .then(response => {
        if (response != null) {
          msal.setActiveAccount(response.account);
        }
        globalServices.machineService.send({ type: MSAL_READY});
      })
      .catch(error => {

        if (error instanceof AuthError) {
          // case: user cancelled entering self-asserted information
          if (error.errorMessage.startsWith('AADB2C90091')) {
            globalServices.machineService.send({ type: MSAL_READY});
            return;
          }
        }

        setError(() => {
          // trick to handle async state with error boundaries
          throw error;
        })
      })

  }, [globalServices]);

  const { corporateId: root } = useAppStore()
  return state.matches("onboarded") ? (
    <QueryClientProvider client={queryClient}>
      <Suspense fallback={ <PageLoader/> } >
        <BrowserRouter>
          <Routes>
            <Route path={root} element={<Layout/>}>
              <Route index element={<DashboardPage/>} />
              <Route path={PERSONAL_DATA_PAGE} element={<PersonalDataPage/>}/>
              <Route path={PAYROLL_DATA_PAGE} element={<PayrollDataPage />}>
                <Route index element={<PayrollResultsView />}/>
                <Route path=":seqId" element={<PayrollDetailsView />}/>
              </Route>
            </Route>
            <Route path="*" element={<Navigate to={root} />} />
          </Routes>
        </BrowserRouter>
      </Suspense>
    </QueryClientProvider>
  ) : (
    <OnboardingPage />
  );
}
