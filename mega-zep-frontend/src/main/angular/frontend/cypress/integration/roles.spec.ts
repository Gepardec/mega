import '../support/commands';

describe('Menu bar', () => {

  const suiteFixtures = [
    '@getInfo',
    '@getUser'
  ];

  beforeEach(() => {
    cy.server();

    cy.fixture('common/info.json').then(jsonData => {
      cy.route('http://localhost:8080/info', jsonData).as('getInfo');
    });

    cy.fixture('common/config.json').then(jsonData => {
      cy.route('http://localhost:8080/config', jsonData).as('getConfig');
    });

    cy.fixture('common/monthendreports.json').then(jsonData => {
      cy.route('http://localhost:8080/worker/monthendreports', jsonData).as('getMonthendreports');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should display all tabs for all roles', () => {
    mockUserJson('common/user.json');
    visitAndWaitForRequests();

    const tabs = cy.get('.mat-tab-links div');

    tabs.should('have.length', 3)
      .first().should('have.text', ' Mein MEGA ')
      .next().should('have.text', ' Office Management ')
      .next().should('have.text', ' Projekt Management ');
  });

  it('should display "Mein MEGA" tab for role employee', () => {
    mockUserJson('menu-bar/user-employee.json');
    visitAndWaitForRequests();

    assertSingleTabShown(' Mein MEGA ');
  });

  it('should display "Office Management" tab for role office management', () => {
    mockUserJson('menu-bar/user-office-management.json');
    visitAndWaitForRequests();

    assertSingleTabShown(' Office Management ');
  });

  it('should display "Projekt Management" tab for role project lead', () => {
    mockUserJson('menu-bar/user-project-lead.json');
    visitAndWaitForRequests();

    assertSingleTabShown(' Projekt Management ');
  });

  it('should display no tab for no role', () => {
    mockUserJson('menu-bar/user-no-role.json');
    visitAndWaitForRequests();

    const tabs = cy.get('.mat-tab-links div');

    tabs.should('have.length', 0);
  });

  function visitAndWaitForRequests(fixtures?: string[]) {
    cy.visit('/');
    cy.wait(fixtures ? fixtures : suiteFixtures);
  }

  function mockUserJson(path: string) {
    cy.fixture(path).then(jsonData => {
      cy.route('http://localhost:8080/user', jsonData).as('getUser')
    });
  }

  function assertSingleTabShown(text: string) {
    const tabs = cy.get('.mat-tab-links div');
    tabs.should('have.length', 1)
      .first().should('have.text', text);
  }
});
