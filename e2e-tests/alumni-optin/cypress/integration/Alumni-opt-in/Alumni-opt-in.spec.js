describe('Alumni Opt-in integration test', () => {

  it('Test Alumni opt-in flow', () => {
    // login and open Alumni opt-in app
    const oUser = Cypress.env('userWithAlumniRole');
    cy.loginDevPortal(oUser.user, oUser.password)
    cy.openAlumni()

    // consent button
    cy.get('[id$=-oButConsent]').should('be.visible').click()

    // expiration date field
    cy.get('[id$=-oInDateExp-inner]').should('be.visible')
    cy.get('[id$=-oButAuthorize]').should('be.visible')

    cy.get('[id$=-oButAuthorize]').click()
    cy.ui5Get('[id$=-oInDateExp]').then((oControl) => {
      cy.wrap(oControl.getValueState()).should('eq', 'Error')
    })

    // authorize button
    cy.get('[id$=-oInDateExp-inner]').type('29.04.2030')
    cy.get('[id$=-oButAuthorize]').click()

    // IDP page with iframe
    cy.get('iframe[id$="oauthIDP"]', { timeout: 15000 }).its('0.contentDocument.body').should('not.be.empty')

    // consent given
    cy.ui5Get('[id$=-oHeaderConsentGiven]').then((oControl) => {
      cy.wrap(oControl.getSubtitle()).should('contain', '29.04.2030')
    })

    // open action
    cy.get('[id$=-oButOpen]').should('be.visible')

    // revoke action
    cy.get('[id$=-oButRevoke]').should('be.visible').click()

    // confirm dialog
    cy.get('#__confirm0').should('be.visible')
    cy.get('#__mbox-btn-0').should('be.visible').click()

    // consent button
    cy.get('[id$=-oButConsent]').should('be.visible')

  })

  it('Test that Alumni opt-in app is not available for user without authorization role', () => {
    // login
    const oUser = Cypress.env('userWithNoAlumniRole');
    cy.loginDevPortal(oUser.user, oUser.password)

    // wait for while loading indicator is hidden
    cy.wait(1000)
    cy.get('.pg-loading-logo', { timeout: 25000 }).should('not.exist')

    // check Alumni opt-in action button does not exist for user
    cy.get('#AppCacheShellUser', { timeout: 10000 }).click()
    cy.get('#AppCacheUserActionZalAccess').click()
    cy.get('#AppCacheAlumniOptin').should('not.to.exist')
  })

})
