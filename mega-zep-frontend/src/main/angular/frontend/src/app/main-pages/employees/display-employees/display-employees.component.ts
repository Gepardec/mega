import {Component, OnDestroy, OnInit} from '@angular/core';
import {DisplayEmployeesService} from "./display-employees.service";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../../../signin/authentication.service";
import {Employee} from "../../../models/Employee/Employee";
import {Subscription} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-display-employees',
  templateUrl: './display-employees.component.html',
  styleUrls: ['./display-employees.component.scss']
})
export class DisplayEmployeesComponent implements OnInit, OnDestroy {

  selectedEmployees: Array<Employee> = new Array<Employee>();
  isGridlistActive: boolean = false;
  user: SocialUser;
  employees: Array<Employee> = new Array<Employee>();
  filteredEmployees: Array<Employee>;
  selectedDate: string = null;

  private currentUserSubscription: Subscription;
  private getEmployeeSubscription: Subscription;
  private selectedEmployeesSubscription: Subscription;
  private updateEmployeesSubscription: Subscription;

  constructor(
    private displayEmployeeListService: DisplayEmployeesService,
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

    this.selectedEmployeesSubscription = this.displayEmployeeListService.selectedEmployees
      .subscribe((selectedEmployees: Array<Employee>) => {
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
      this.updateEmployeesSubscription = this.displayEmployeeListService.updateEmployees(this.selectedEmployees, this.selectedDate)
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
      this.getEmployeeSubscription = this.displayEmployeeListService.getEmployees(this.user)
        .subscribe((employee: Array<Employee>) => {
          this.employees = employee;
          this.filteredEmployees = this.employees;
          this.selectedEmployees = new Array<Employee>();
          this.displayEmployeeListService.setSelectedEmployees(null);
          this.displayEmployeeListService.setResetSelection(true);
        });
    }
  }

  dateChanged(date: string) {
    this.selectedDate = date;
  }

  applyFilter(filterValue: string) {
    this.filteredEmployees = this.employees
      .filter(empl => empl.vorname.toLowerCase().includes(filterValue.toLowerCase())
        || empl.nachname.toLowerCase().includes(filterValue.toLowerCase()));
  }

  openSnackBar(message: string) {
    this.snackbar.open(message, null, {
      duration: 3000,
    });
  }

}
