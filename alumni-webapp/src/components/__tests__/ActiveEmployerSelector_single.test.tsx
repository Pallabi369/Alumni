import React from "react";
import {render, screen} from '@testing-library/react';
import ActiveEmployerSelector from "../ActiveEmployerSelector";
import {QueryClient, QueryClientProvider} from "react-query";
import {appStore} from "../../store/appStore";
import {BrowserRouter} from "react-router-dom";
import {I18nextProvider, initReactI18next} from "react-i18next";
import i18n from "i18next";

i18n
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    resources: { en: { translations: {} } }
  });

describe('Single employment', () => {
  it('when single employer', async () => {

    appStore().setCorporateId("");

    const queryClient = new QueryClient();
    await queryClient.fetchQuery('employmentHistory', () => {
      return {
        data: {
          employments: [
            {
              zalarisId: "510-001",
              companyCode: "510",
              companyName: "Zalaris",
              startDate: "2019-01-01",
              endDate: "99991231"
            },
          ]
        }
      }
    });

    render(
      <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <I18nextProvider i18n={i18n}>
            <ActiveEmployerSelector/>
          </I18nextProvider>
        </BrowserRouter>
      </QueryClientProvider>
    );

    expect(screen.getByText('Zalaris')).toBeTruthy();
    expect(screen.queryByRole('button')).toBeNull();
    expect(screen.queryByRole('presentation')).toBeVisible();
    expect(appStore().employment!.zalarisId).toBe("510-001");

  });
});
