import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {User} from '../../models/User';
import {InfoDialogComponent} from '../info-dialog/info-dialog.component';
import {OAuthService} from 'angular-oauth2-oidc';
import {UserInfo} from '../../../monthly-report/models/UserInfo';
import {MatMenuTrigger} from '@angular/material/menu';

@Component({
  selector: 'app-user-actions',
  templateUrl: './user-actions.component.html',
  styleUrls: ['./user-actions.component.scss']
})
export class UserActionsComponent implements OnInit {

  @Input() user: User;
  @Output() logout: EventEmitter<void> = new EventEmitter();
  @ViewChild(MatMenuTrigger) menuTrigger: MatMenuTrigger;

  pictureUrl: string;

  constructor(private dialog: MatDialog,
              private oAuthService: OAuthService) {
  }

  ngOnInit(): void {
    this.oAuthService.loadUserProfile().then((userInfo: UserInfo) => {
      this.pictureUrl = userInfo?.info?.picture;
    });
  }

  doLogout() {
    this.logout.emit();
  }

  openInfoDialog(): void {
    this.dialog.open(InfoDialogComponent, {minWidth: '50%', autoFocus: false});
  }
}
