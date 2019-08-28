import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../signin/authentication.service";
import {SocialUser} from "angularx-social-login";
import {HomeService} from "../home.service";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Component({
  selector: 'app-display-employee',
  templateUrl: './display-employee.component.html',
  styleUrls: ['./display-employee.component.scss']
})
export class DisplayEmployeeComponent implements OnInit {

  user: SocialUser;
  employee: MitarbeiterType;

  constructor(
    private authService: AuthenticationService,
    private homeService: HomeService
  ) {
  }

  ngOnInit() {
    this.authService.currentUser.subscribe(
      (user: SocialUser) => {
        this.user = user;
        this.getEmployee();
      }
    );
  }

  getEmployee() {
    this.homeService.getMitarbeiter(this.user)
      .subscribe((employee => this.employee = employee));
  }

}
