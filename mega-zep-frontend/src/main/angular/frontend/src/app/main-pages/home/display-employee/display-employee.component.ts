import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../signin/authentication.service";
import {SocialUser} from "angularx-social-login";
import {HomeService} from "../home.service";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-display-employee',
  templateUrl: './display-employee.component.html',
  styleUrls: ['./display-employee.component.scss']
})
export class DisplayEmployeeComponent implements OnInit, OnDestroy {
  user: SocialUser;
  employee: MitarbeiterType;

  private currentUserSubscription: Subscription;
  private getEmployeeSubscription: Subscription;

  constructor(
    private authService: AuthenticationService,
    private homeService: HomeService
  ) {
  }

  ngOnInit() {
    this.currentUserSubscription = this.authService.currentUser.subscribe(
      (user: SocialUser) => {
        this.user = user;
        if (this.user) {
          this.getEmployee();
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
    this.getEmployeeSubscription && this.getEmployeeSubscription.unsubscribe();
  }

  getEmployee() {
    this.getEmployeeSubscription = this.homeService.getEmployee(this.user)
      .subscribe((employee => this.employee = employee));
  }

}
