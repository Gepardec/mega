import {Component, OnDestroy, OnInit} from '@angular/core';
import {DisplayEmployeeListService} from "./display-employee-list.service";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {Subscription} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-display-employee-list',
  templateUrl: './display-employee-list.component.html',
  styleUrls: ['./display-employee-list.component.scss']
})
export class DisplayEmployeeListComponent implements OnInit, OnDestroy {

  selectedEmployees: Array<MitarbeiterType> = new Array<MitarbeiterType>();
  isGridlistActive: boolean = false;
  user: SocialUser;
  employees: MitarbeiterResponseType;
  filteredEmployees: Array<MitarbeiterType>;
  selectedDate: string = null;

  private currentUserSubscription: Subscription;
  private getEmployeeSubscription: Subscription;
  private selectedEmployeesSubscription: Subscription;
  private updateEmployeesSubscription: Subscription;

  constructor(
    private displayMitarbeiterListeService: DisplayEmployeeListService,
    private authenticationService: AuthenticationService,
    private snackbar: MatSnackBar
  ) {

  }

  ngOnInit() {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      this.getAllEmployees();
    });

    this.selectedDate = null;

    this.selectedEmployeesSubscription = this.displayMitarbeiterListeService.selectedEmployees
      .subscribe((selectedEmployees: Array<MitarbeiterType>) => {
        this.selectedEmployees = selectedEmployees;
      });

  }

  ngOnDestroy(): void {
    this.currentUserSubscription && this.currentUserSubscription.unsubscribe();
    this.selectedEmployeesSubscription && this.selectedEmployeesSubscription.unsubscribe();
    this.getEmployeeSubscription && this.getEmployeeSubscription.unsubscribe();
    this.updateEmployeesSubscription && this.updateEmployeesSubscription.unsubscribe();
  }

  toggleView(): void {
    this.isGridlistActive = !this.isGridlistActive;
  }

  releaseEmployees() {
    if (this.selectedDate) {
      this.updateEmployeesSubscription = this.displayMitarbeiterListeService.updateEmployees(this.selectedEmployees, this.selectedDate)
        .subscribe((res) => {
          // refresh employees
          this.employees = null;
          this.getAllEmployees();
          this.openSnackBar("Mitarbeiter erfolgreich aktualisiert!");
        });
    }
  }

  getAllEmployees() {
    if (this.user) {
      this.getEmployeeSubscription = this.displayMitarbeiterListeService.getEmployees(this.user)
        .subscribe((mitarbeiter: MitarbeiterResponseType) => {
          this.employees = mitarbeiter;
          this.filteredEmployees = this.employees.mitarbeiterListe.mitarbeiter;
          this.selectedEmployees = new Array<MitarbeiterType>();
          this.displayMitarbeiterListeService.setSelectedEmployees(null);
          this.displayMitarbeiterListeService.setResetSelection(true);
        });
    }
  }

  dateChanged(date: string) {
    this.selectedDate = date;
  }

  applyFilter(filterValue: string) {
    this.filteredEmployees = this.employees.mitarbeiterListe.mitarbeiter
      .filter(empl => empl.vorname.toLowerCase().includes(filterValue.toLowerCase())
        || empl.nachname.toLowerCase().includes(filterValue.toLowerCase()));
  }

  openSnackBar(message: string) {
    this.snackbar.open(message, null, {
      duration: 3000,
    });
  }

}
