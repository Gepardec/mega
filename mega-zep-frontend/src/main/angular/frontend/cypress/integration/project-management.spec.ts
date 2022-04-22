describe('Projekt Management', () => {

  beforeEach(() => {
    cy.server();

    cy.fixture('common/info.json').then(jsonData => {
      cy.route('http://localhost:8080/info', jsonData).as('getInfo');
    });

    cy.fixture('common/user.json').then(jsonData => {
      cy.route('http://localhost:8080/user', jsonData).as('getUser');
    });

    cy.fixture('common/config.json').then(jsonData => {
      cy.route('http://localhost:8080/config', jsonData).as('getConfig');
    });

    cy.fixture('project-management/projectcomments.json').then(jsonData => {
      cy.route('http://localhost:8080/projectcomments?date=**-**-**&projectName=Cash-Cow-Project', jsonData).as('getProjectcomments');
    });

    // @ts-ignore
    cy.loginByGoogleApi();
  });

  it('should display that the employee has not confirmed his bookings', () => {
    cy.fixture('project-management/projectmanagemententries.json').then(jsonData => {
      jsonData[0].entries[0].employeeCheckState = 'OPEN';
      jsonData[0].entries[0].customerCheckState = 'OPEN';
      jsonData[0].entries[0].internalCheckState = 'OPEN';
      cy.route('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
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
      cy.route('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
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
      // TODO project-state
      cy.route('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
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
      // TODO project-state
      cy.route('http://localhost:8080/management/projectmanagemententries/**/**?all=false', jsonData).as('getProjectmanagemententries');
    });

    visitAndWaitForRequests();

    assertDropdownContent('project-controlling', 'Fertig');
    assertDropdownContent('billing', 'Fertig');
    // TODO project-state
    // assertDropdownContent('project-state', 'Fertig');
  });

  it('should display user comments and can add new ones', () => {
    cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false',
      { fixture: 'project-management/projectmanagemententries.json' }
    ).as('getProjectmanagemententries')
    cy.fixture('project-management/create-employee-comment.json').then(jsonData => {
      cy.route('POST', 'http://localhost:8080/comments', jsonData).as('create-employee-comment');
    });
    cy.intercept('http://localhost:8080/comments/getallcommentsforemployee?email=**&date=**', []
    ).as('employee-comments-empty')

    visitAndWaitForRequests();

    // TODO assert -/- displayed

    cy.get('[data-cy="open-comments"]').click();
    cy.wait('@employee-comments-empty');

    // getallcommentsforemployee will return comments for further requests
    cy.intercept('http://localhost:8080/comments/getallcommentsforemployee?email=**&date=**',
      { fixture: 'project-management/employee-comments.json' }
    ).as('employee-comments')
    // projectmanagemententries will contain comments for further requests
    cy.intercept('http://localhost:8080/management/projectmanagemententries/**/**?all=false',
      { fixture: 'project-management/projectmanagemententries-with-comments.json' }
    ).as('getProjectmanagemententriesWithComments')

    cy.get('app-comments-for-employee textarea').type('Hallo Chuck Norris!');
    cy.get('app-comments-for-employee [data-cy="add-comment"]').click();
    cy.wait('@employee-comments');
    cy.wait('@getProjectmanagemententriesWithComments');

    // TODO assert comment content

    cy.get('[data-cy="close"]').click();

    // TODO assert 0/1 displayed
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

