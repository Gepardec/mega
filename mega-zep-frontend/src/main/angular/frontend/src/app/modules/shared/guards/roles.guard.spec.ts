import { TestBed } from '@angular/core/testing';
import { RolesGuard } from './roles.guard';
import { RolesService } from '../services/roles/roles.service';

describe('RolesGuard', () => {
  let guard: RolesGuard;

  class RolesServiceMock {
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: RolesService, useClass: RolesServiceMock
        }
      ]
    });
    guard = TestBed.inject(RolesGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
