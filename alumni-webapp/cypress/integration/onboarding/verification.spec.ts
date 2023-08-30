import {appendZalarisId, deleteClaimSsid} from "../../support/auth";

describe('Verification', () => {
  const { users: { pristine }} = Cypress.env();
  beforeEach(function () {
    sessionStorage.clear();
    cy.login(pristine);
  });

  it('transit to verification required', () => {
    cy.visit('');
    cy.waitForReact();
    cy.react('IdentityVerificationStep').get('a').contains("Verify");
  })

  it('no transit to verification if office user', () => {
    deleteClaimSsid();
    appendZalarisId('510-12345678');
    cy.visit('/510');
    cy.waitForReact();
    cy.react('PageLoader').should('exist');
  })

  it('transit to verification_error_code: invalid code', () => {
    cy.visit("/redirect?code=invalid");
    cy.waitForReact();
    cy.react('InvalidVerificationCode').should('exist');
  })

  it('transit to verification_error_code: no code', () => {
    cy.visit("/redirect");
    cy.waitForReact();
    cy.react('InvalidVerificationCode').should('exist');
  })

  it('transit to verification_error_code: restart verification process', () => {
    cy.visit("/redirect");
    cy.waitForReact();
    cy.react('InvalidVerificationCode').get('button').click();
    cy.waitForReact();
    cy.react('IdentityVerificationStep').should('exist');
  })
})
