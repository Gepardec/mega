import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../models/User';
import { MatDialog } from '@angular/material/dialog';
import { InfoDialogComponent } from '../../info-dialog/info-dialog.component';
import { User } from '../../models/User';

@Component({
  selector: 'app-user-actions',
  templateUrl: './user-actions.component.html',
  styleUrls: ['./user-actions.component.scss']
})
export class UserActionsComponent {

  @Input()
  user: User;

  @Output()
  logout: EventEmitter<void> = new EventEmitter();

  constructor(private dialog: MatDialog) {
  }

  doLogout() {
    this.logout.emit();
  }

  openInfoDialog(): void {
    this.dialog.open(InfoDialogComponent, {minWidth: '50%', autoFocus: false});
  }
}
