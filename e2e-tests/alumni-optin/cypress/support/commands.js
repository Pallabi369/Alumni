// get UI5 control out of DOM
Cypress.Commands.add("ui5Get", (sIdControl) => {
  cy.get(sIdControl, { timeout: 10000 }).then(
    (oDOMControl) => {
      return new Cypress.Promise((resolve, reject) => {
        var oUI5Control = oDOMControl.control()[0]
        resolve(oUI5Control)
      })
    })
})


// login to Zalaris devportal
Cypress.Commands.add('loginDevPortal', (user, password) => {
  cy.request({
    method: 'POST',
    url: 'https://devportal.zalaris.com/nea/v1/authenticate?neaRelayState=ZHDPORTAL%3ahttps%3a%2f%2fdevportal.zalaris.com%2fep%2fredirect%3fservice%3dneptune',
    body: {
      j_username: user,
      j_password: password,
      uidPasswordLogon: "Log On",
      login_submit: "on",
      login_do_redirect: "1"
    },
    form: true
  })

  cy.visit('https://devportal.zalaris.com/ep/redirect?service=neptune')
})

// open alumni opt-in app
Cypress.Commands.add('openAlumni', (user, password) => {
  // wait for splash screen
  cy.wait(1000)
  cy.get('.pg-loading-logo', { timeout: 25000 }).should('not.exist')

  // check Opt-in for Alumni action exists for user and click
  cy.get('#AppCacheShellUser', { timeout: 10000 }).should('exist').click()
  cy.get('#AppCacheUserActionZalAccess').should('exist').click()
  cy.get('#AppCacheAlumniOptin').should('exist').click({ force: true })

  // alumni opt-in app is opened in dialog
  cy.get('#__dialog0').should('exist')
})