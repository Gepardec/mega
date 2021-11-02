import {TestBed} from '@angular/core/testing';

import {RolesService} from './roles.service';
import {RouterTestingModule} from '@angular/router/testing';
import {UserService} from '../user/user.service';
import {BehaviorSubject} from 'rxjs';
import {User} from '../../models/User';
import {configuration} from '../../constants/configuration';
import {Role} from '../../models/Role';
import {routes} from '../../../../app-routing.module';
import {MonthlyReportModule} from '../../../monthly-report/monthly-report.module';
import {OfficeManagementModule} from '../../../office-management/office-management.module';
import {ProjectManagementModule} from '../../../project-management/project-management.module';

describe('RolesService', () => {
  let userSubject: BehaviorSubject<User> = new BehaviorSubject<User>(undefined);

  class UserServiceMock {
    user: BehaviorSubject<User> = userSubject;
  }

  class MockComponent {

  }

  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      MonthlyReportModule,
      OfficeManagementModule,
      ProjectManagementModule,
      RouterTestingModule.withRoutes(routes)
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

  describe('on common', () => {
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
        roles: [Role.EMPLOYEE]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT + 'somewrongroute')).toBe(false);
    });
  });

  describe('on employee', () => {
    it('should return true if user roles are sufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.MONTHLY_REPORT)).toBe(true);
    });

    it('should return false if user roles are insufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
    });
  });

  describe('on office managment', () => {
    it('should return true if user roles are sufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE, Role.OFFICE_MANAGEMENT]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(true);
    });

    it('should return false if user roles are sufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE, Role.OFFICE_MANAGEMENT]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.PROJECT_MANAGEMENT)).toBe(false);
    });
  });

  describe('on project lead', () => {
    it('should return true if user roles are sufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE, Role.PROJECT_LEAD]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.PROJECT_MANAGEMENT)).toBe(true);
    });

    it('should return false if user roles are sufficient', () => {
      const service: RolesService = TestBed.get(RolesService);
      userSubject.next({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE, Role.PROJECT_LEAD]
      });
      expect(service.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
    });
  });
});
