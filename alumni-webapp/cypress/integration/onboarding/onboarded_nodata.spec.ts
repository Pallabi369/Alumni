import {appendZalarisId} from "../../support/auth";

describe('Onboarded with no data scenario', () => {
  const { users: { nooptin  }} = Cypress.env();
  beforeEach(() => {
    sessionStorage.clear();
    cy.login(nooptin);
  });

  it('home: no data found', () => {
    cy.visit('/any');
    cy.waitForReact();
    cy.react('NoDataStep').should('exist');
  })

  it('corporate: no data found', () => {
    appendZalarisId('510-12345678');
    cy.visit('/510/any');
    cy.waitForReact();
    cy.react('NoDataStep').should('exist');
  });

})
