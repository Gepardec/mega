import { TestBed } from '@angular/core/testing';

import { LoginGuard } from './login.guard';
import { UserService } from '../services/user/user.service';
import { BehaviorSubject } from 'rxjs';
import { User } from '../models/User';
import { RouterTestingModule } from '@angular/router/testing';
import { routes } from '../../../app-routing.module';
import { MonthlyReportModule } from '../../monthly-report/monthly-report.module';
import { OfficeManagementModule } from '../../office-management/office-management.module';
import { ProjectManagementModule } from '../../project-management/project-management.module';

describe('LoginGuard', () => {
  let guard: LoginGuard;

  const userSubject: BehaviorSubject<User> = new BehaviorSubject<User>(undefined);

  class UserServiceMock {
    user: BehaviorSubject<User> = userSubject;
  }

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
    guard = TestBed.inject(LoginGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
