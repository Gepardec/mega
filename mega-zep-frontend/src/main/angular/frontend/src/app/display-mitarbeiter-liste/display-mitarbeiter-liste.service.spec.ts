import { TestBed } from '@angular/core/testing';

import { DisplayMitarbeiterListeService } from './display-mitarbeiter-liste.service';

describe('DisplayMitarbeiterListeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DisplayMitarbeiterListeService = TestBed.get(DisplayMitarbeiterListeService);
    expect(service).toBeTruthy();
  });
});
