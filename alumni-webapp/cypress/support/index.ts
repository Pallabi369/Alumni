// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

import 'cypress-react-selector';
import { login } from "./auth";

declare global {
  namespace Cypress {
    interface Chainable {
      login({username, password}): Chainable<Element>
    }
  }
}

// @ts-ignore
Cypress.Commands.add("login", ({username, password}) => {
  return login({username, password});
})
