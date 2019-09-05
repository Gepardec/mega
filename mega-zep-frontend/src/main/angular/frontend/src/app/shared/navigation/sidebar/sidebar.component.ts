import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {MatSidenav} from "@angular/material";
import {MainLayoutService} from "../../main-layout/main-layout/main-layout.service";
import {configuration} from "../../../../configuration/configuration";
import {Subscription} from "rxjs";
import {AuthenticationService} from "../../../signin/authentication.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit, AfterViewInit, OnDestroy {

  readonly login: string = configuration.PAGES.LOGIN;
  readonly home: string = configuration.PAGES.HOME;
  readonly dashboard: string = configuration.PAGES.DASHBOARD;
  readonly employees: string = configuration.PAGES.EMPLOYEES;

  @ViewChild('sidenav', {static: false}) sidenav: MatSidenav;

  showFiller = false;

  private toggleSidenavSubscription: Subscription;

  constructor(
    private router: Router,
    private mainLayoutService: MainLayoutService,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    this.toggleSidenavSubscription = this.mainLayoutService.toggleSidenav.subscribe(
      (newState: boolean) => {
        setTimeout(() => {
          this.sidenav.toggle();
        }, 100);
      }
    );
  }

  ngOnDestroy(): void {
    this.toggleSidenavSubscription && this.toggleSidenavSubscription.unsubscribe();
  }

  isEmployeeAdminOrController(): boolean {
    return this.authenticationService.isEmployeeAdminOrController();
  }

  routeTo(location: string): void {
    this.router.navigate([location]);
  }
}
