describe('Mein Mega', () => {

  beforeEach(() => {
    cy.server();

    cy.fixture('common/info.json').then(jsonData => {
      cy.route('http://localhost:8080/info', jsonData).as('getInfo');
    });

    cy.fixture('common/monthendreports.json').then(jsonData => {
      cy.route('http://localhost:8080/worker/monthendreports', jsonData).as('getMonthendreports');
    });

    cy.fixture('common/user.json').then(jsonData => {
      cy.route('http://localhost:8080/user', jsonData).as('getUser');
    });

    cy.fixture('common/config.json').then(jsonData => {
      cy.route('http://localhost:8080/config', jsonData).as('getConfig');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
    cy.visit('/');
    cy.wait(['@getInfo', '@getMonthendreports', '@getUser']);
  });


  it('Should confirm the bookings for the selected month', () => {
    cy.route({
      method: 'PUT',
      url: 'http://localhost:8080/stepentry/close',
      response: true
    }).as('updateEmployeeCheck');

    cy.fixture('common/monthendreports.json').then(jsonData => {
      cy.log(jsonData);
      jsonData.employeeCheckState = 'DONE';
      cy.route('http://localhost:8080/worker/monthendreports/*/*', jsonData).as('getMonthendreportsDone')
    });

    cy.get('app-employee-check .mat-card')
      .should('not.be.null');
    cy.get('app-employee-check .mat-card .mat-card-title')
      .should('have.text', 'Status zum Monatsabschluss');

    cy.get('app-employee-check .mat-card mat-card-content button')
      .should('have.text', 'Buchungen bestätigen')
      .click();

    cy.wait('@getMonthendreportsDone');

    cy.get('app-employee-check .mat-card mat-card-content button')
      .should('not.exist')
  });
});
