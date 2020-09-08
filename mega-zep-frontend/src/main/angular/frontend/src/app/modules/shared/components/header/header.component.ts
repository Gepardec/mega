import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../services/user/user.service';
import { Observable, Subscription } from 'rxjs';
import { User } from '../../models/User';
import { Link } from '../../models/Link';
import { RolesService } from '../../services/roles/roles.service';
import { TranslateService } from '@ngx-translate/core';
import { configuration } from '../../constants/configuration';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  isHandset: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(map(result => result.matches));

  readonly links = new Array<Link>();
  readonly spreadSheetUrl = configuration.SPREADSHEET_URL;
  readonly zepUrl = configuration.ZEP_URL;
  readonly assetsPath = '../../../../../assets/';
  readonly logoMega = 'logo-MEGA.png';
  readonly excelLogo = 'excel-logo.png';
  readonly zepLogo = 'zep-eye.png';
  user: User;
  private userSubscription: Subscription;

  constructor(private rolesService: RolesService,
              private userService: UserService,
              private translate: TranslateService,
              private breakpointObserver: BreakpointObserver) {
    translate.get('PAGE_NAMES').subscribe(
      PAGE_NAMES => {
        this.links.push({name: PAGE_NAMES.MONTHLY_REPORT, path: configuration.PAGE_URLS.MONTHLY_REPORT});
        this.links.push({name: PAGE_NAMES.EMPLOYEES, path: configuration.PAGE_URLS.EMPLOYEES});
        console.log(this.links);
      }
    );
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
