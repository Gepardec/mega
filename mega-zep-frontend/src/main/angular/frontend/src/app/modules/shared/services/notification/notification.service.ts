import { Injectable } from '@angular/core';
import {MatSnackBar, MatSnackBarRef, SimpleSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  snackBarRef: MatSnackBarRef<SimpleSnackBar>;

  constructor(private snackbar: MatSnackBar) {
  }

  showSuccess(message: string): void {
    this.snackbar.open(message);
  }

  showError(message: string): void {
    if(!this.snackBarRef) {
      // The second parameter is the text in the button.
      // In the third, we send in the css class for the snack bar.
      this.snackBarRef = this.snackbar.open(message, 'X', {panelClass: ['error']});
    }
  }
}
