describe('Dashboard', () => {
  const {users: {main}} = Cypress.env();

  beforeEach(() => {
    sessionStorage.clear();
    cy.login(main);
  });

  it('main page', () => {
    cy.visit("/");
    cy.waitForReact();
    cy.react('DashboardPage').should('exist');
  })
});
