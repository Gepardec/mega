import { TestBed } from '@angular/core/testing';

import { RolesService } from './roles.service';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from '../user/user.service';
import { BehaviorSubject } from 'rxjs';
import { User } from '../../models/User';
import { configuration } from '../../constants/configuration';
import { Role } from '../../models/Role';

describe('RolesService', () => {
  let userSubject: BehaviorSubject<User> = new BehaviorSubject<User>(undefined);

  class UserServiceMock {
    user: BehaviorSubject<User> = userSubject;
  }

  class MockComponent {

  }

  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      RouterTestingModule.withRoutes([
        {
          path: configuration.PAGE_URLS.MONTHLY_REPORT,
          component: MockComponent
        },
        {
          path: configuration.PAGE_URLS.OFFICE_MANAGEMENT,
          data: {
            roles: [Role.ADMINISTRATOR, Role.CONTROLLER]
          },
          component: MockComponent
        },
      ])
    ],
    providers: [
      {
        provide: UserService, useClass: UserServiceMock
      }
    ]
  }));

  beforeEach(() => {
    userSubject = new BehaviorSubject<User>(undefined);
  });

  it('should return false if no user is logged in', () => {
    const service: RolesService = TestBed.get(RolesService);
    expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
  });

  it('should return false if route is not found', () => {
    const service: RolesService = TestBed.get(RolesService);
    userSubject.next({
      userId: '07-johndoe',
      firstname: 'john',
      lastname: 'doe',
      email: 'john.doe@gepardec.com',
      role: Role.USER,
      pictureUrl: undefined
    });
    expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT + 'somewrongroute')).toBe(false);
  });

  it('should return false if user roles are insufficient', () => {
    const service: RolesService = TestBed.get(RolesService);
    userSubject.next({
      userId: '07-johndoe',
      firstname: 'john',
      lastname: 'doe',
      email: 'john.doe@gepardec.com',
      role: Role.USER,
      pictureUrl: undefined
    });
    expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
  });

  it('should return true if user roles are sufficient', () => {
    const service: RolesService = TestBed.get(RolesService);
    userSubject.next({
      userId: '07-johndoe',
      firstname: 'john',
      lastname: 'doe',
      email: 'john.doe@gepardec.com',
      role: Role.CONTROLLER,
      pictureUrl: undefined
    });
    expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(true);
  });

  it('should return true if no role is required', () => {
    const service: RolesService = TestBed.get(RolesService);
    userSubject.next({
      userId: '07-johndoe',
      firstname: 'john',
      lastname: 'doe',
      email: 'john.doe@gepardec.com',
      role: Role.USER,
      pictureUrl: undefined
    });
    expect(service.isAllowed(configuration.PAGE_URLS.MONTHLY_REPORT)).toBe(true);
  });
});
