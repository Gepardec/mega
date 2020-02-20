import { Component, OnDestroy, OnInit } from '@angular/core';
import { configuration } from '../../constants/configuration';
import { UserService } from '../../services/user/user.service';
import { Subscription } from 'rxjs';
import { User } from '../../models/User';
import { Link } from '../../models/Link';
import { RolesService } from '../../services/roles/roles.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  private userSubscription: Subscription;

  readonly links: Array<Link> = [{
    name: configuration.PAGE_NAMES.MONTHLY_REPORT,
    path: configuration.PAGE_URLS.MONTHLY_REPORT
  }, {
    name: configuration.PAGE_NAMES.EMPLOYEES,
    path: configuration.PAGE_URLS.EMPLOYEES
  }];
  readonly spreadSheetUrl = configuration.SPREADSHEET_URL;
  readonly zepUrl = configuration.ZEP_URL;
  readonly assetsPath = '../../../../../assets/';
  readonly logoMega = 'LogoMEGA.png';
  readonly excelLogo = 'excelLogo.png';
  readonly zepLogo = 'zepEyeClean.png';

  user: User;

  constructor(private rolesService: RolesService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.userSubscription = this.userService.user.subscribe((user) => {
      this.user = user;
    });
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  showLink(link: Link) {
    return this.rolesService.isAllowed(link.path);
  }

  onLogout($event: void) {
    this.userService.logout();
  }
}
