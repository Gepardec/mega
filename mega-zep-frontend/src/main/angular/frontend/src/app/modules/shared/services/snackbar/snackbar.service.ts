import {Injectable} from '@angular/core';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {configuration} from '../../constants/configuration';

export type SnackbarConfig = {
  horizontalPosition: MatSnackBarHorizontalPosition,
  verticalPosition: MatSnackBarVerticalPosition,
  duration: number
}

@Injectable({
  providedIn: 'root'
})
export class SnackbarService {

  private message = this.translate.instant('snackbar.message');
  private action = this.translate.instant('snackbar.confirm');
  private config: SnackbarConfig = {
    horizontalPosition: <MatSnackBarHorizontalPosition>configuration.snackbar.horizontalPosition,
    verticalPosition: <MatSnackBarVerticalPosition>configuration.snackbar.verticalPosition,
    duration: configuration.snackbar.duration
  }

  constructor(private _snackBar: MatSnackBar,
              private translate: TranslateService) {
  }

  showDefaultSnackbar() {
    this.showSnackbar(this.message, this.action, this.config);
  }

  showSnackbarWithMessage(message: string) {
    this.showSnackbar(message, this.action, this.config);
  }

  showSnackbar(message: string, action: string, config?: SnackbarConfig) {
    this._snackBar.open(message, action, config ? config : this.config);
  }
}
