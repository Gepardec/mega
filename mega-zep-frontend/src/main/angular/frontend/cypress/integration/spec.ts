import '../support/commands';

describe('Sample Test', () => {

  beforeEach(() => {
    cy.fixture('info.json').as('infoJSON');
    cy.fixture('config.json').as('configJSON');
    cy.fixture('monthendreports.json').as('monthendreportsJSON');
    cy.fixture('user.json').as('userJSON');

    cy.server();

    cy.route('http://localhost:8080/info', '@infoJSON').as('info');
    cy.route('http://localhost:8080/config', '@configJSON').as('config');
    cy.route('http://localhost:8080/worker/monthendreports', '@monthendreportsJSON').as('monthendreports');
    cy.route('http://localhost:8080/user', '@userJSON').as('user');


    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should ...', () => {
    cy.visit('/');
  });

});
