// @ts-ignore
import enterprise from '../fixtures/officemanagement/enterpriseentries.json';

describe('Office Management (Unternehmen)', () => {

  const suiteFixtures = [
    '@getInfo',
    '@getUser',
    '@getEnterpriseEntries',
    '@getProjectManagementEntries',
    '@getOfficeManagementEntries',
    '@getProjectComments'
  ];

  beforeEach(() => {
    cy.fixture('common/info.json').then(jsonData => {
      cy.intercept('http://localhost:*/info', jsonData).as('getInfo');
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    cy.fixture('officemanagement/projectmanagemententries.json').then(jsonData => {
      cy.intercept('http://localhost:*/management/projectmanagemententries/*/*?all=true', jsonData).as('getProjectManagementEntries');
    });

    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      cy.intercept('http://localhost:*/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    cy.fixture('officemanagement/projectcomments.json').then(jsonData => {
      cy.intercept('http://localhost:*/projectcomments?date=**-**-**&projectName=Cash-Cow-Project', jsonData).as('getProjectComments');
    });

    cy.fixture('common/user.json').then(jsonData => {
      cy.intercept('http://localhost:*/user', jsonData).as('getUser');
    });

    cy.fixture('common/config.json').then(jsonData => {
      cy.intercept('http://localhost:*/config', jsonData).as('getConfig');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should display all enterprise entries open', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('zep-times-released', 'Offen');
    assertSelect('chargeability-external-employees', 'Offen');
    assertSelect('payroll-accounting-sent', 'Offen');
    assertSelect('zep-monthly-report', 'Offen');
  });

  it('should display zep times release date done when state "done" gets selected', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('zep-times-released', 'Offen');

    cy.intercept('PUT', 'http://localhost:*/enterprise/entry/*/*', {
      body: true
    }).as('updateEnterpriseEntry');

    cy.get('[data-cy="zep-times-released"]').click().get('[data-cy="option-done"]').click();

    cy.get('@updateEnterpriseEntry').its('request.body').should('deep.equal', {
      ...enterprise,
      zepTimesReleased: 'DONE'
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      jsonData.zepTimesReleased = 'DONE';
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('zep-times-released', 'Fertig')
      .get('.mat-select')
      .should('have.class', 'mat-select-disabled');
  });

  it('should display chargeability of external employees in progress when state "in progress" gets selected', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('chargeability-external-employees', 'Offen');

    cy.intercept('PUT', 'http://localhost:*/enterprise/entry/*/*', {
      body: true
    }).as('updateEnterpriseEntry');

    cy.get('[data-cy="chargeability-external-employees"]').click().get('[data-cy="option-in-progress"]').click();

    cy.get('@updateEnterpriseEntry').its('request.body').should('deep.equal', {
      ...enterprise,
      chargeabilityExternalEmployeesRecorded: 'WORK_IN_PROGRESS'
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      jsonData.chargeabilityExternalEmployeesRecorded = 'WORK_IN_PROGRESS';
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('chargeability-external-employees', 'In Arbeit');
  });

  it('should display payroll accounting sent not relevant when "not relevant" gets selected', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('payroll-accounting-sent', 'Offen');

    cy.intercept('PUT', 'http://localhost:*/enterprise/entry/*/*', {
      body: true
    }).as('updateEnterpriseEntry');

    cy.get('[data-cy="payroll-accounting-sent"]').click().get('[data-cy="option-not-relevant"]').click();

    cy.get('@updateEnterpriseEntry').its('request.body').should('deep.equal', {
      ...enterprise,
      payrollAccountingSent: 'NOT_RELEVANT'
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      jsonData.payrollAccountingSent = 'NOT_RELEVANT';
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('payroll-accounting-sent', 'Nicht relevant');
  });

  it('should display zep monthly report done', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('zep-monthly-report', 'Offen');

    cy.intercept('PUT', 'http://localhost:*/enterprise/entry/*/*', {
      body: true
    }).as('updateEnterpriseEntry');

    cy.get('[data-cy="zep-monthly-report"]').click().get('[data-cy="option-done"]').click();

    cy.get('@updateEnterpriseEntry').its('request.body').should('deep.equal', {
      ...enterprise,
      zepMonthlyReportDone: 'DONE'
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      jsonData.zepMonthlyReportDone = 'DONE';
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('zep-monthly-report', 'Fertig')
      .get('.mat-select')
      .should('have.class', 'mat-select-disabled');
  });

  it('should display all enterprise entries done when checks in response are set to "done"', () => {
    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      jsonData.zepTimesReleased = 'DONE';
      jsonData.chargeabilityExternalEmployeesRecorded = 'DONE';
      jsonData.payrollAccountingSent = 'DONE';
      jsonData.zepMonthlyReportDone = 'DONE';
      cy.intercept('http://localhost:*/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    visitAndWaitForRequests('/officeManagement');
    assertSelect('zep-times-released', 'Fertig');
    assertSelect('chargeability-external-employees', 'Fertig');
    assertSelect('payroll-accounting-sent', 'Fertig');
    assertSelect('zep-monthly-report', 'Fertig');
  });

  function assertSelect(attribute: 'zep-times-released' | 'chargeability-external-employees' | 'payroll-accounting-sent' | 'zep-monthly-report', text: string) {
    return cy.get('[data-cy="' + attribute + '"]')
      .should('be.visible')
      .should('have.text', text);
  }

  function visitAndWaitForRequests(endpoint: string, fixtures?: string[]) {
    cy.visit(endpoint);
    cy.wait(fixtures ? fixtures : suiteFixtures);
  }
});
