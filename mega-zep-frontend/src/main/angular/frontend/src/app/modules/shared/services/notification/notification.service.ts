import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private router: Router,
              private snackbar: MatSnackBar) {
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(event => this.snackbar.dismiss());
  }

  showSuccess(message: string): void {
    this.snackbar.open(message, 'OK', {duration: 3000});
  }

  showError(message: string): void {
    this.snackbar.open(message, 'X', {panelClass: ['error']});
  }
}
