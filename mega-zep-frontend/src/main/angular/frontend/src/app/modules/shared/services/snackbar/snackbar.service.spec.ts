import {TestBed} from '@angular/core/testing';

import {SnackbarConfig, SnackbarService} from './snackbar.service';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {
  MatSnackBar,
  MatSnackBarHorizontalPosition,
  MatSnackBarModule,
  MatSnackBarVerticalPosition
} from '@angular/material/snack-bar';
import {configuration} from "../../constants/configuration";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('SnackbarService', () => {

  let snackbarService: SnackbarService;
  let matSnackBar: MatSnackBar;
  let translateService: TranslateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        MatSnackBarModule,
        TranslateModule.forChild(),
        BrowserAnimationsModule
      ]
    });

    snackbarService = TestBed.inject(SnackbarService);
    matSnackBar = TestBed.inject(MatSnackBar);
    translateService = TestBed.inject(TranslateService);
  });

  it('#should be created', () => {
    expect(snackbarService).toBeTruthy();
  });

  it('#showDefaultSnackbar - should call snackBar.open with default values', () => {
    spyOn(matSnackBar, 'open').and.callThrough();

    snackbarService.showDefaultSnackbar();

    expect(matSnackBar.open).toHaveBeenCalledWith(translateService.instant('snackbar.message'), translateService.instant('snackbar.confirm'), SnackbarConfigMock.config);
  });

  it('#showSnackbarWithMessage - should call snackBar.open with message', () => {
    spyOn(matSnackBar, 'open').and.callThrough();

    snackbarService.showSnackbarWithMessage(SnackbarConfigMock.message);

    expect(matSnackBar.open).toHaveBeenCalledWith(SnackbarConfigMock.message, translateService.instant('snackbar.confirm'), SnackbarConfigMock.config);
  });

  it('#showSnackbar - should call snackBar.open', () => {
    spyOn(matSnackBar, 'open').and.callThrough();

    snackbarService.showSnackbar(SnackbarConfigMock.message, SnackbarConfigMock.action, SnackbarConfigMock.config);

    expect(matSnackBar.open).toHaveBeenCalledWith(SnackbarConfigMock.message, SnackbarConfigMock.action, SnackbarConfigMock.config);
  });

  class SnackbarConfigMock {

    static message: string = 'message';
    static action: string = 'action'

    static config: SnackbarConfig = {
      horizontalPosition: <MatSnackBarHorizontalPosition>configuration.snackbar.horizontalPosition,
      verticalPosition: <MatSnackBarVerticalPosition>configuration.snackbar.verticalPosition,
      duration: configuration.snackbar.duration
    }
  }
});
