import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { User } from '../../../models/User';

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

  constructor() {
  }

  doLogout() {
    this.logout.emit();
  }
}
