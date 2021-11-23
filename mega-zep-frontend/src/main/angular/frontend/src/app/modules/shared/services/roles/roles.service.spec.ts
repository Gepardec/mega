import {TestBed} from '@angular/core/testing';

import {RolesService} from './roles.service';
import {RouterTestingModule} from '@angular/router/testing';
import {UserService} from '../user/user.service';
import {configuration} from '../../constants/configuration';
import {Role} from '../../models/Role';
import {routes} from '../../../../app-routing.module';
import {MonthlyReportModule} from '../../../monthly-report/monthly-report.module';
import {OfficeManagementModule} from '../../../office-management/office-management.module';
import {ProjectManagementModule} from '../../../project-management/project-management.module';
import {Router} from "@angular/router";
import {User} from "../../models/User";
import {BehaviorSubject} from "rxjs";

describe('RolesService', () => {

  let rolesService: RolesService;
  let userService: UserService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
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
    });

    rolesService = TestBed.inject(RolesService);
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
  });

  describe('on common', () => {
    it('should return false if no user is logged in', () => {
      expect(rolesService.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
    });

    it('should return false if route is not found', () => {

      spyOnProperty(userService.user, 'value').and.returnValue({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE]
      });

      expect(rolesService.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT + 'somewrongroute')).toBe(false);
    });
  });

  describe('on employee', () => {
    it('should return true if user roles are sufficient', () => {

      spyOnProperty(userService.user, 'value').and.returnValue({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE]
      });

      expect(rolesService.isAllowed(configuration.PAGE_URLS.MONTHLY_REPORT)).toBe(true);
    });

    it('should return false if user roles are insufficient', () => {

      spyOnProperty(userService.user, 'value').and.returnValue({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE]
      });

      expect(rolesService.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
    });
  });

  describe('on office managment', () => {
    it('should return true if user roles are sufficient', () => {

      spyOnProperty(userService.user, 'value').and.returnValue(
        {
          userId: '07-johndoe',
          firstname: 'john',
          lastname: 'doe',
          email: 'john.doe@gepardec.com',
          roles: [Role.EMPLOYEE, Role.OFFICE_MANAGEMENT]
        }
      );

      expect(rolesService.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(true);
    });

    it('should return false if user roles are sufficient', () => {

      spyOnProperty(userService.user, 'value').and.returnValue({
        userId: '07-johndoe',
        firstname: 'john',
        lastname: 'doe',
        email: 'john.doe@gepardec.com',
        roles: [Role.EMPLOYEE, Role.OFFICE_MANAGEMENT]
      });

      expect(rolesService.isAllowed(configuration.PAGE_URLS.PROJECT_MANAGEMENT)).toBe(false);
    });
  });

  describe('on project lead', () => {
    it('should return true if user roles are sufficient', () => {

      spyOnProperty(userService.user, 'value').and.returnValue(
        {
          userId: '07-johndoe',
          firstname: 'john',
          lastname: 'doe',
          email: 'john.doe@gepardec.com',
          roles: [Role.EMPLOYEE, Role.PROJECT_LEAD]
        }
      );

      expect(rolesService.isAllowed(configuration.PAGE_URLS.PROJECT_MANAGEMENT)).toBe(true);
    });

    it('should return false if user roles are sufficient', () => {
      spyOnProperty(userService.user, 'value').and.returnValue(
        {
          userId: '07-johndoe',
          firstname: 'john',
          lastname: 'doe',
          email: 'john.doe@gepardec.com',
          roles: [Role.EMPLOYEE, Role.PROJECT_LEAD]
        }
      );

      expect(rolesService.isAllowed(configuration.PAGE_URLS.OFFICE_MANAGEMENT)).toBe(false);
    });
  });

  class UserServiceMock {

    user: BehaviorSubject<User> = new BehaviorSubject<User>(undefined);

  }
});
