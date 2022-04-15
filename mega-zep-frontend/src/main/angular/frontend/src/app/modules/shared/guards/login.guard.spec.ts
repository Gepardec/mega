import {TestBed, waitForAsync} from '@angular/core/testing';

import {LoginGuard} from './login.guard';
import {UserService} from '../services/user/user.service';
import {BehaviorSubject} from 'rxjs';
import {User} from '../models/User';
import {RouterTestingModule} from '@angular/router/testing';
import {routes} from '../../../app-routing.module';
import {MonthlyReportModule} from '../../monthly-report/monthly-report.module';
import {OfficeManagementModule} from '../../office-management/office-management.module';
import {ProjectManagementModule} from '../../project-management/project-management.module';
import {expect} from '@angular/flex-layout/_private-utils/testing';

describe('LoginGuard', () => {

  let guard: LoginGuard;
  let userService: UserService;

  const userSubject: BehaviorSubject<User> = new BehaviorSubject<User>(undefined);

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        MonthlyReportModule,
        OfficeManagementModule,
        ProjectManagementModule,
        RouterTestingModule.withRoutes(routes)
      ],
      providers: [
        {provide: UserService, useClass: UserServiceMock}
      ]
    }).compileComponents().then(() => {
      guard = TestBed.inject(LoginGuard);
      userService = TestBed.inject(UserService);
    });
  }));

  it('#should create', () => {
    expect(guard).toBeTruthy();
  });

  it('#canActivate - should be logged in', () => {
    spyOn(userService, 'loggedInWithGoogle').and.returnValue(true);
    spyOnProperty(userService.user, 'value').and.returnValue(true);

    const canActivate = guard.canActivate(null, null);

    expect(userService.loggedInWithGoogle).toHaveBeenCalled();
    expect(canActivate).toBeTrue();
  });

  it('#canActivate - should not be logged in', () => {
    spyOn(userService, 'loggedInWithGoogle').and.returnValue(false);
    spyOn(userService, 'setStartpageOverride').and.stub();

    const canActivate = guard.canActivate(null, createMockStartPage('localhost:4020'));

    expect(userService.loggedInWithGoogle).toHaveBeenCalled();
    expect(canActivate).toBeFalse();
  });

  class UserServiceMock {

    user: BehaviorSubject<User> = userSubject;

    loggedInWithGoogle() {
    }

    setStartpageOverride() {
    }
  }

  const createMockStartPage = (id: string) => {
    return {
      state: {url: id}
    } as any;
  }
});
