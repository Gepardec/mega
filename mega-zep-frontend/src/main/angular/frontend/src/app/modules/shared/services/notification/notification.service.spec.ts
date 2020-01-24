import { TestBed } from '@angular/core/testing';

import { NotificationService } from './notification.service';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('NotificationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      MatSnackBarModule
    ]
  }));

  it('should be created', () => {
    const service: NotificationService = TestBed.get(NotificationService);
    expect(service).toBeTruthy();
  });
});
