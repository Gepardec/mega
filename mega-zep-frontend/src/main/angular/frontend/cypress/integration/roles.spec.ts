import '../support/commands';

describe('Menu bar', () => {

  beforeEach(() => {
    cy.server();

    cy.fixture('common/info.json').as('infoJSON');
    cy.fixture('common/config.json').as('configJSON');
    cy.fixture('common/monthendreports.json').as('monthendreportsJSON');

    cy.route('http://localhost:8080/info', '@infoJSON').as('info');
    cy.route('http://localhost:8080/config', '@configJSON').as('config');
    cy.route('http://localhost:8080/worker/monthendreports', '@monthendreportsJSON').as('monthendreports');

    // @ts-ignore
    cy.loginByGoogleApi();
    cy.visit('/');
  });

  it('should display all tabs for all roles', () => {
    mockUserJson('common/user.json');

    const tabs = cy.get('.mat-tab-links div');

    tabs.should('have.length', 3)
      .first().should('have.text', ' Mein MEGA ')
      .next().should('have.text', ' Office Management ')
      .next().should('have.text', ' Projekt Management ');
  });

  it('should display "Mein MEGA" tab for role employee', () => {
    mockUserJson('menu-bar/user-employee.json');

    assertSingleTabShown(' Mein MEGA ');
  });

  it('should display "Office Management" tab for role office management', () => {
    mockUserJson('menu-bar/user-office-management.json');

    assertSingleTabShown(' Office Management ');
  });

  it('should display "Projekt Management" tab for role project lead', () => {
    mockUserJson('menu-bar/user-project-lead.json');

    assertSingleTabShown(' Projekt Management ');
  });

  it('should display no tab for no role', () => {
    mockUserJson('menu-bar/user-no-role.json');

    const tabs = cy.get('.mat-tab-links div');

    tabs.should('have.length', 0);
  });

});

function mockUserJson(path: string) {
  cy.fixture(path).as('userJSON');
  cy.route('http://localhost:8080/user', '@userJSON').as('user');
}

function assertSingleTabShown(text: string) {
  const tabs = cy.get('.mat-tab-links div');
  tabs.should('have.length', 1)
    .first().should('have.text', text);
}
