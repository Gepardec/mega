import { TestBed } from '@angular/core/testing';

import { LoaderService } from './loader.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { OverlayModule } from '@angular/cdk/overlay';

describe('LoaderService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      OverlayModule,
      MatProgressSpinnerModule
    ]
  }));

  it('should be created', () => {
    const service: LoaderService = TestBed.get(LoaderService);
    expect(service).toBeTruthy();
  });
});
