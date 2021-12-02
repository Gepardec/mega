import {TestBed} from '@angular/core/testing';

import {LoaderService} from './loader.service';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {OverlayModule} from '@angular/cdk/overlay';
import {AngularMaterialModule} from "../../../material/material-module";

describe('LoaderService', () => {

  let loaderService: LoaderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        OverlayModule,
        MatProgressSpinnerModule,
        AngularMaterialModule
      ]
    });

    loaderService = TestBed.inject(LoaderService);
  });

  it('#should be created', () => {
    expect(loaderService).toBeTruthy();
  });

  it('#showSpinner() - should create cdk-overlay-container', () => {
    loaderService.showSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(true);
  });

  it('#showSpinner() - should not attach second time', () => {
    loaderService.showSpinner();
    loaderService.showSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(true);
  });

  it('#stopSpinner() - should delete cdk-overlay-container', () => {
    loaderService.showSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(true);

    loaderService.stopSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(false);
  });

  it('#stopSpinner() - should should call detach only second time', () => {
    loaderService.showSpinner();
    loaderService.showSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(true);

    loaderService.stopSpinner();
    loaderService.stopSpinner();

    expect(loaderService.spinnerRef.hasAttached()).toBe(false);
  });
});
