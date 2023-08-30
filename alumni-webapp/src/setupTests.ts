// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';
import 'jest-location-mock';
import crypto from 'crypto';

// @ts-ignore
global.window.__RUNTIME_CONFIG__ = {
  "BASE_PATH": "http://127.0.0.1:5000",
  "MSAL_CLIENT_ID": "",
  "MSAL_PRIVATE_AUTHORITY": "b2c_1_login",
  "MSAL_CORPORATE_AUTHORITY": "b2c_1_business",
  "MSAL_REDIRECT_URI": "",
  "MSAL_KNOWN_AUTHORITIES": "",
  "SIGNICAT_CLIENT_ID": "",
  "SIGNICAT_REDIRECT_URI": "http://localhost:5000/redirect"
};

// @ts-ignore
global.crypto = crypto;
