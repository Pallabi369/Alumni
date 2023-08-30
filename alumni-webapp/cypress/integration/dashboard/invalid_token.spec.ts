import {appendZalarisId} from "../../support/auth";

describe('Onboarded scenario', () => {
  const { users: { main  }} = Cypress.env();
  beforeEach(() => {
    sessionStorage.clear();
    cy.login(main);
  });

  it('incomplete token detection', () => {
    cy.visit('/510');
    cy.waitForReact();
    cy.react('InvalidToken').should('exist');
  });

  it('incomplete token detection', () => {
    appendZalarisId('510-12345678')
    cy.visit('/');
    cy.waitForReact();
    cy.react('InvalidToken').should('exist');
  });


})
