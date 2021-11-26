import {TestBed} from '@angular/core/testing';

import {NotificationService} from './notification.service';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {RouterTestingModule} from '@angular/router/testing';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('NotificationService', () => {

  let notificationSerice: NotificationService;
  let snackBar: MatSnackBar;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        BrowserAnimationsModule
      ]
    });

    notificationSerice = TestBed.inject(NotificationService);
    snackBar = TestBed.inject(MatSnackBar);
  });

  it('#should be created', () => {
    expect(notificationSerice).toBeTruthy();
  });

  it('#showSuccess - should call MatSnackBar::open with success', () => {
    const snackBarSpy = spyOn(snackBar, 'open');

    notificationSerice.showSuccess("message");

    expect(snackBarSpy).toHaveBeenCalledWith(NotificationsMock.message, NotificationsMock.actionOk, {duration: 3000});
  });

  it('#showError - should call MatSnackBar::open with error', () => {
    const snackBarSpy = spyOn(snackBar, 'open');

    notificationSerice.showError(NotificationsMock.message);

    expect(snackBarSpy).toHaveBeenCalledWith(NotificationsMock.message, NotificationsMock.actionError, {panelClass: ['error']});
  });

  class NotificationsMock {

    static message: string = 'message';
    static actionOk: string = 'OK';
    static actionError: string = 'X';

  }
});
