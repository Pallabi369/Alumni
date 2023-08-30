import {fireEvent, render, screen, within} from "@testing-library/react";
import React from "react";
import {UserAvatar} from "../UserAvatar";
import i18n from "i18next";
import {I18nextProvider, initReactI18next} from "react-i18next";
import {appStore} from "../../store/appStore";

i18n
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    resources: { en: { translations: {} } }
  });

test('home user avatar and menu', async () => {
  appStore().setCorporateId("");

  render(
    <I18nextProvider i18n={i18n}>
      <UserAvatar name={'John Do'} bgColor={'pink'}/>
    </I18nextProvider>
  );

  expect(screen.getByText('JD')).toBeTruthy();
  fireEvent.click(screen.getByText('JD'));
  expect(screen.getByText('common.logout')).toBeTruthy();
});

test('home corporate avatar and menu', async () => {
  appStore().setCorporateId("510");

  render(
    <I18nextProvider i18n={i18n}>
      <UserAvatar name={'Dirty Harry'} bgColor={'pink'}/>
    </I18nextProvider>
  );

  expect(screen.getByText('DH')).toBeTruthy();
  fireEvent.click(screen.getByText('DH'));
  expect(screen.queryByText('common.logout')).toBeNull();
});