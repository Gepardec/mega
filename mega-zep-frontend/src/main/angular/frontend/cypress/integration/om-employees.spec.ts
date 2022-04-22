// @ts-ignore
import employee from '../fixtures/officemanagement/officemanagemententries.json';

describe('Office Management (Mitarbeiter)', () => {

  const suiteFixtures = [
    '@getInfo',
    '@getUser',
    '@getEnterpriseEntries',
    '@getProjectManagementEntries',
    '@getOfficeManagementEntries',
    '@getProjectComments'
  ];

  beforeEach(() => {
    cy.server();

    cy.fixture('common/info.json').then(jsonData => {
      cy.route('http://localhost:8080/info', jsonData).as('getInfo');
    });

    cy.fixture('officemanagement/enterpriseentries.json').then(jsonData => {
      cy.route('http://localhost:8080/enterprise/entriesformonthyear/*/*', jsonData).as('getEnterpriseEntries');
    });

    cy.fixture('officemanagement/projectmanagemententries.json').then(jsonData => {
      cy.route('http://localhost:8080/management/projectmanagemententries/*/*?all=true', jsonData).as('getProjectManagementEntries');
    });

    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    cy.fixture('officemanagement/projectcomments.json').then(jsonData => {
      cy.route('http://localhost:8080/projectcomments?date=**-**-**&projectName=Cash-Cow-Project', jsonData).as('getProjectComments');
    });

    cy.fixture('common/user.json').then(jsonData => {
      cy.route('http://localhost:8080/user', jsonData).as('getUser');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should contain one element in employee table in card with title "Mitarbeiter"', () => {
    visitAndWaitForRequests('/officeManagement');
    cy.get('[data-cy="employee-card"] mat-card-title').should('have.text', 'Mitarbeiter');
    cy.get('[data-cy="employee-table"]').should('have.length', 1);
  });

  it('should display all employee checks "open"', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('customer-check', 'Offen');
    assertSelect('internal-check', 'Offen');
    assertCheck('employee-check', 'cancel');
    assertCheck('project-check', 'cancel');
  });

  it('should change the status of "Kundenzeiten" when "Fertig" gets selected', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('customer-check', 'Offen');

    cy.route({
      method: 'PUT',
      url: 'http://localhost:8080/stepentry/closeforoffice',
      response: true
    }).as('closeforoffice');

    cy.get('[data-cy="customer-check"]').click().get('[data-cy="option-done"]').click();

    cy.get('@closeforoffice').its('request.body').should('deep.include', {
      stepId: 3,
      employee: {
        ...employee[0].employee
      }
    });

    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].customerCheckState = 'DONE';
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('customer-check', 'Fertig');
  });

  it('should change the status of "Interne Zeiten" when "Fertig" gets selected', () => {
    visitAndWaitForRequests('/officeManagement');
    assertSelect('internal-check', 'Offen');

    cy.route({
      method: 'PUT',
      url: 'http://localhost:8080/stepentry/closeforoffice',
      response: true
    }).as('putRequest');

    cy.get('[data-cy="internal-check"]').click().get('[data-cy="option-done"]').click();

    cy.get('@putRequest').its('request.body').should('deep.include', {
      stepId: 2,
      employee: {
        ...employee[0].employee
      }
    });

    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].internalCheckState = 'DONE';
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    assertSelect('internal-check', 'Fertig');
  });

  it('should display that the employee confirmed his bookings', () => {
    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].employeeCheckState = 'DONE';
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');
    assertCheck('employee-check', 'check_circle');
  });

  it('should display that the project lead confirmed the employees bookings', () => {
    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].projectCheckState = 'DONE';
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');
    assertCheck('project-check', 'check_circle');
  });

  it('should display all employee checks done when checks in response are set to "done"', () => {
    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].customerCheckState = 'DONE';
      jsonData[0].internalCheckState = 'DONE';
      jsonData[0].employeeCheckState = 'DONE';
      jsonData[0].projectCheckState = 'DONE';
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');
    assertSelect('customer-check', 'Fertig');
    assertSelect('internal-check', 'Fertig');
    assertCheck('employee-check', 'check_circle');
    assertCheck('project-check', 'check_circle');
  });

  it('should indicate that there is one comment present for the employee', () => {
    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].totalComments = 1;
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    cy.get('[data-cy="comment-indicator"]')
      .should('contain.text', '0 / 1')
      .children('span')
      .should('have.class', 'red');
  });

  it('should indicate that there is one resolved comment out of one for the employee', () => {
    cy.fixture('officemanagement/officemanagemententries.json').then(jsonData => {
      jsonData[0].totalComments = 1;
      jsonData[0].finishedComments = 1;
      cy.route('http://localhost:8080/management/officemanagemententries/*/*', jsonData).as('getOfficeManagementEntries');
    });

    visitAndWaitForRequests('/officeManagement');

    cy.get('[data-cy="comment-indicator"]')
      .should('contain.text', '1 / 1')
      .children('span')
      .should('have.class', 'green');
  });

  function assertCheck(attribute: 'employee-check' | 'internal-check' | 'customer-check' | 'project-check', icon: 'cancel' | 'check_circle') {
    cy.get('[data-cy="' + attribute + '"] mat-icon')
      .should('be.visible')
      .should('have.text', icon);
  }

  function assertSelect(attribute: string, text: string) {
    cy.get('[data-cy="' + attribute + '"]')
      .should('be.visible')
      .should('have.text', text);
  }

  function visitAndWaitForRequests(endpoint: string, fixtures?: string[]) {
    cy.visit(endpoint);
    cy.wait(fixtures ? fixtures : suiteFixtures);
  }
});
