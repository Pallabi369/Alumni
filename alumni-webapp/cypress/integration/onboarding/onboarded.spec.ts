import {appendZalarisId, deleteClaimSsid} from "../../support/auth";

describe('Onboarded scenario', () => {
  const { users: { main  }} = Cypress.env();
  beforeEach(() => {
    sessionStorage.clear();
    cy.login(main);
  });

  it('home: cross-routing test', () => {
    cy.visit('/');
    cy.waitForReact();
    cy.react('DashboardPage').should('exist');

    cy.visit('/510');
    cy.waitForReact();
    cy.react('InvalidToken').should('exist');
  })

  it('corporate: cross-routing test', () => {
    appendZalarisId('510-12345678');
    cy.visit('/510');
    cy.waitForReact();
    cy.react('DashboardPage').should('exist');

    cy.visit('/');
    cy.waitForReact();
    cy.react('InvalidToken').should('exist');
  })

  it('transit to verification results after loss ssid claims', () => {
    expect(deleteClaimSsid()).to.be.true
    sessionStorage.setItem("waiting_for_ssid_claim_key", "true");

    cy.visit("");
    cy.waitForReact();
    cy.react('VerificationResult').should('exist');
  });

})

