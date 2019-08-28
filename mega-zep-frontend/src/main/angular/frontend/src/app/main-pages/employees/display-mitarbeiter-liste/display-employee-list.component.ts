import {Component, Input, OnInit} from '@angular/core';
import {DisplayEmployeeListService} from "./display-employee-list.service";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Component({
  selector: 'app-display-employee-list',
  templateUrl: './display-employee-list.component.html',
  styleUrls: ['./display-employee-list.component.scss']
})
export class DisplayEmployeeListComponent implements OnInit {

  selectedEmployees: Array<MitarbeiterType> = new Array<MitarbeiterType>();

  protected isGridlistActive: boolean = true;

  @Input('SocialUser') user: SocialUser;

  employees: MitarbeiterResponseType;

  constructor(
    private displayMitarbeiterListeService: DisplayEmployeeListService,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      console.log(this.user.lastName);
      console.log(this.user.firstName);
      console.log(this.user.email);
      this.displayMitarbeiterListeService.getMitarbeiter(this.user)
        .subscribe((mitarbeiter: MitarbeiterResponseType) => {
          this.employees = mitarbeiter;
        });
    });

    this.displayMitarbeiterListeService.selectedEmployees
      .subscribe((selectedEmployees: Array<MitarbeiterType>) => {
        this.selectedEmployees = selectedEmployees;
      });

  }

  toggleView(): void {
    this.isGridlistActive = !this.isGridlistActive;
  }

  releaseEmployees() {
    console.log(this.selectedEmployees);
    this.displayMitarbeiterListeService.updateMitarbeiter(this.selectedEmployees)
      .subscribe((res) => {
        console.log(res);
      });
  }

}
