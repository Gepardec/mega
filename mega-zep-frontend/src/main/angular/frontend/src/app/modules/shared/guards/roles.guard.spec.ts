import {TestBed, waitForAsync} from '@angular/core/testing';
import {RolesGuard} from './roles.guard';
import {RolesService} from '../services/roles/roles.service';

describe('RolesGuard', () => {

  let guard: RolesGuard;
  let rolesService: RolesService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: RolesService, useClass: RolesServiceMock}
      ]
    }).compileComponents().then(() => {
      guard = TestBed.inject(RolesGuard);
      rolesService = TestBed.inject(RolesService);
    });
  }));

  it('#should create', () => {
    expect(guard).toBeTruthy();
  });

  it('#canActivate - should return true', () => {
    spyOn(rolesService, 'isAllowed').and.returnValue(true);

    const canActivate = guard.canActivate(createMockRoute('localhost:4020'), null);

    expect(rolesService.isAllowed).toHaveBeenCalled();
    expect(canActivate).toBeTrue();
  });

  class RolesServiceMock {
    isAllowed() {
    }
  }

  const createMockRoute = (id: string) => {
    return {
      routeConfig: {path: id}
    } as any;
  }
});
