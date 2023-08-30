import React from "react";
import {fireEvent, render, screen, within} from '@testing-library/react';
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

describe('Multiple employments', () => {
  it('changing active employer', async () => {

    appStore().setCorporateId("");

    const queryClient = new QueryClient();
    await queryClient.fetchQuery('employmentHistory', () => {
      return {
        data: {
          employments: [
            {zalarisId: "510-001", companyCode: "510", companyName: "Zalaris", startDate: "2019-01-01", endDate: "99991231"},
            {zalarisId: "420-001", companyCode: "420", companyName: "McDonalds", startDate: "2017-01-01", endDate: "2019-01-01"},
            {zalarisId: "330-001", companyCode: "330", companyName: "Cleaning service", startDate: "2016-01-01", endDate: "2017-01-01"},
          ]
        }
      }
    });

    render(
      <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <I18nextProvider i18n={i18n}>
            <ActiveEmployerSelector />
          </I18nextProvider>
        </BrowserRouter>
      </QueryClientProvider>
    );

    expect(within(screen.getByRole('button')).getByText('Zalaris')).toBeTruthy();
    expect(appStore().employment!.zalarisId).toBe("510-001");

    fireEvent.click(screen.getByText('Zalaris'));

    const items = await screen.findAllByRole("menuitem");
    expect(items).toHaveLength(3);

    fireEvent.click(screen.getByText('McDonalds'));
    const menu = await screen.findByRole('menu');
    expect(within(menu).getByText('McDonalds')).toBeTruthy();

    expect(appStore().employment!.zalarisId).toBe("420-001");

  });

});
