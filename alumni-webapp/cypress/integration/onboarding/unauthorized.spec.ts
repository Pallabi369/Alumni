import {PERSONAL_DATA_PAGE} from "../../../src/routes";
import {APP_STORE} from "../../../src/store/appStore";

describe('Unauthorized users', () => {

  beforeEach(() => {
    sessionStorage.clear();
  });

  it('denied access to /any resources as home user', () => {
    cy.visit('/any');
    cy.waitForReact();
    cy.react('GetStartedStep').should('exist');
  })

  it('denied access to /510/any resources as office user', () => {
    cy.visit('/510/any');
    cy.waitForReact();
    cy.react('WelcomeOfficeUser').should('exist');
  })

  it('denied access to existing resources as home user', () => {
    cy.visit(PERSONAL_DATA_PAGE);
    cy.waitForReact();
    cy.react('GetStartedStep').should('exist');
  })

  it('denied access to existing resources as office user', () => {
    cy.visit('/510/' + PERSONAL_DATA_PAGE);
    cy.waitForReact();
    cy.react('WelcomeOfficeUser').should('exist');
  })

  it('load private welcome page', () => {
    cy.visit("");
    cy.waitForReact();
    cy.react('GetStartedStep').get('button').contains("Sign in");
    cy.window().then(window => {
      const appStore = JSON.parse(window.sessionStorage.getItem(APP_STORE));
      expect(appStore.corporateId).to.be.eq('');
    });
  })

  it('load corporate welcome page', () => {
    cy.visit("/510");
    cy.waitForReact();
    cy.react('WelcomeOfficeUser').should('exist');
    cy.window().then(window => {
      const appStore = JSON.parse(window.sessionStorage.getItem(APP_STORE));
      expect(appStore.corporateId).to.be.eq('510');
    });
  })

});
