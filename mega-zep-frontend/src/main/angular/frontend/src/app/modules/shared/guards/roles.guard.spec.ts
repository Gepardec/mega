import { RolesGuard } from './roles.guard';

describe('RolesGuard', () => {

  /*beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        AppModule
      ],
      providers: [
        RolesGuard
      ]
    });
  });

  function setupRoute(): ActivatedRouteSnapshot {
    let route = new ActivatedRouteSnapshot();
    route.url = new Array<UrlSegment>();
    route.url.push(new UrlSegment("/employees", null));
    route.data = {roles: [configuration.EMPLOYEE_ROLES.ADMINISTRATOR, configuration.EMPLOYEE_ROLES.CONTROLLER]};
    return route;
  }

  function setupUser(rights: number): Employee {
    let employee: Employee = new Employee();
    employee.firstName = "Max";
    employee.sureName = "Mustermann";
    employee.releaseDate = "2019-01-01";
    employee.role = rights;

    return employee;
  }

  it('should create', inject([RolesGuard], (guard: RolesGuard) => {
    expect(guard).toBeTruthy();
  }));

  it('should not be activated by user equals null', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    expect(guard.canActivate(route, null)).toEqual(false);
  }));

  it('should not be activated by user with no rights', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    let employee = setupUser(configuration.EMPLOYEE_ROLES.USER);
    guard.employee = employee;
    expect(guard.canActivate(route, null)).toEqual(false);
  }));

  it('should be activated by user with rights', inject([RolesGuard], (guard: RolesGuard) => {
    let route = setupRoute();
    let employee = setupUser(configuration.EMPLOYEE_ROLES.ADMINISTRATOR);
    guard.employee = employee;
    expect(guard.canActivate(route, null)).toEqual(true);
  }));*/
});


