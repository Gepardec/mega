import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../../services/user/user.service';
import {Observable, Subscription} from 'rxjs';
import {User} from '../../models/User';
import {Link} from '../../models/Link';
import {RolesService} from '../../services/roles/roles.service';
import {TranslateService} from '@ngx-translate/core';
import {configuration} from '../../constants/configuration';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {map} from 'rxjs/operators';
import {ConfigService} from "../../services/config/config.service";
import {Config} from "../../models/Config";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  readonly links = new Array<Link>();
  readonly assetsPath = '../../../../../assets/';
  readonly logoMega = 'logo-MEGA.png';
  readonly zepLogo = 'zep-eye.png';

  user: User;
  zepUrl: string;

  private userSubscription: Subscription;

  isHandset: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(map(result => result.matches));

  constructor(private rolesService: RolesService,
              private userService: UserService,
              private translate: TranslateService,
              private breakpointObserver: BreakpointObserver,
              private configService: ConfigService) {
    translate.get('PAGE_NAMES').subscribe(PAGE_NAMES => {
      this.links.push({name: PAGE_NAMES.MONTHLY_REPORT, path: configuration.PAGE_URLS.MONTHLY_REPORT});
      this.links.push({name: PAGE_NAMES.OFFICE_MANAGEMENT, path: configuration.PAGE_URLS.OFFICE_MANAGEMENT});
      this.links.push({name: PAGE_NAMES.PROJECT_MANAGEMENT, path: configuration.PAGE_URLS.PROJECT_MANAGEMENT});
    });
  }

  ngOnInit() {
    this.userSubscription = this.userService.user.subscribe((user) => {
      this.user = user;
    });
    this.configService.getConfig().subscribe((config: Config) => {
      this.zepUrl = config.zepOrigin;
    });
  }

  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe();
  }

  showLink(link: Link): boolean {
    return this.rolesService.isAllowed(link.path);
  }

  onLogout(): void {
    this.userService.logout();
  }
}
