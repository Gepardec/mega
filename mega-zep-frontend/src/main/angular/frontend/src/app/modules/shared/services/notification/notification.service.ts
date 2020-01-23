import { Injectable } from '@angular/core';
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private snackbar: MatSnackBar) {
  }

  showSuccess(message: string): void {
    this.snackbar.open(message, null, {duration: 3000});
  }

  showError(message: string): void {
    this.snackbar.open(message, 'X', {panelClass: ['error']});
  }
}
