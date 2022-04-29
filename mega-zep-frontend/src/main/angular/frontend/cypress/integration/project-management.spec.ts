describe('Projekt Management', () => {

  beforeEach(() => {
    cy.fixture('common/info.json').then(jsonData => {
      cy.intercept('http://localhost:8080/info', jsonData).as('getInfo');
    });

    cy.fixture('common/user.json').then(jsonData => {
      cy.intercept('http://localhost:8080/user', jsonData).as('getUser');
    });

    cy.fixture('common/config.json').then(jsonData => {
      cy.intercept('http://localhost:8080/config', jsonData).as('getConfig');
    });

    cy.fixture('project-management/projectcomments.json').then(jsonData => {
      cy.intercept('http://localhost:8080/projectcomments?date=**-**-**&projectName=Cash-Cow-Project', jsonData).as('getProjectcomments');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should display that the employee has not confirmed his bookings', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].entries[0].employeeCheckState = 'OPEN';
      jsonData[0].entries[0].customerCheckState = 'OPEN';
      jsonData[0].entries[0].internalCheckState = 'OPEN';
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
    });

    visitAndWaitForRequests();

    assertCheck('employee-check', 'cancel');
    assertCheck('internal-check', 'cancel');
    assertCheck('customer-check', 'cancel');
  });

  it('should display that the employee has confirmed his bookings', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].entries[0].employeeCheckState = 'DONE';
      jsonData[0].entries[0].customerCheckState = 'DONE';
      jsonData[0].entries[0].internalCheckState = 'DONE';
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
    });

    visitAndWaitForRequests();

    assertCheck('employee-check', 'check_circle');
    assertCheck('internal-check', 'check_circle');
    assertCheck('customer-check', 'check_circle');
  });

  it('should display all other checks as open', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].controlProjectState = 'OPEN';
      jsonData[0].controlBillingState = 'OPEN';
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
    });

    visitAndWaitForRequests();

    assertDropdownContent('project-controlling', 'Offen');
    assertDropdownContent('billing', 'Offen');
    assertDropdownContent('project-state', 'Offen');
  });

  it('should display all other checks as done', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].controlProjectState = 'DONE';
      jsonData[0].controlBillingState = 'DONE';
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
    });

    visitAndWaitForRequests();

    assertDropdownContent('project-controlling', 'Fertig');
    assertDropdownContent('billing', 'Fertig');
  });

  it('should display user comments and can add new ones', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData);
    }).as('getProjectmanagemententries');

    cy.fixture('common/create-employee-comment.json').then(jsonData => {
      cy.intercept('POST', 'http://localhost:8080/comments', jsonData).as('create-employee-comment');
    });

    cy.intercept('http://localhost:8080/comments/getallcommentsforemployee?email=**&date=**', []
    ).as('employee-comments-empty');

    visitAndWaitForRequests();

    cy.get('app-done-comments-indicator').should('contain.text', '− / −');

    cy.get('[data-cy="open-comments"]').click();
    cy.wait('@employee-comments-empty');

    // getallcommentsforemployee will return comments for further requests
    cy.fixture('common/employee-comments.json').then(jsonData => {
      cy.intercept('http://localhost:8080/comments/getallcommentsforemployee?email=**&date=**', jsonData);
    }).as('employee-comments');

    // projectmanagemententries will contain comments for further requests
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].entries[0].totalComments = 1;
      cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData);
    }).as('getProjectmanagemententriesWithComments');

    cy.get('app-comments-for-employee textarea').type('Hallo Chuck Norris!');
    cy.get('app-comments-for-employee [data-cy="add-comment"]').click();
    cy.wait('@employee-comments');
    cy.wait('@getProjectmanagemententriesWithComments');

    cy.get('[data-cy="employee-comments"] td:nth-child(4)').should('contain.text', 'Hallo Chuck Norris!');

    cy.get('[data-cy="close"]').click();

    cy.get('app-done-comments-indicator').should('contain.text', '0 / 1');
  });


  function visitAndWaitForRequests() {
    cy.visit('/projectManagement');
    cy.wait(['@getInfo', '@getUser', '@getProjectmanagemententries', '@getProjectcomments']);
  }

  function assertCheck(attribute: 'employee-check' | 'internal-check' | 'customer-check', icon: 'cancel' | 'check_circle') {
    cy.get('[data-cy="' + attribute + '"] mat-icon')
      .should('be.visible')
      .should('have.text', icon);
  }

  function assertDropdownContent(attribute: string, content: string) {
    cy.get('[data-cy="' + attribute + '"]')
      .should('be.visible')
      .should('have.text', content);
  }
});
