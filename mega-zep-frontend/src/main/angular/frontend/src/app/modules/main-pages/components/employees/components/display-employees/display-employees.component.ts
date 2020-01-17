import {Component, OnDestroy, OnInit} from '@angular/core';
import {Employee} from "../../../../../shared/models/Employee/Employee";
import {SocialUser} from "angularx-social-login";
import {Subscription} from "rxjs";
import {ZepSigninService} from "../../../../../shared/services/signin/zep-signin.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {EmployeesService} from "../../../../../shared/services/employees/employees.service";
import {User} from "../../../../../shared/models/User/User";

@Component({
  selector: 'app-display-employees',
  templateUrl: './display-employees.component.html',
  styleUrls: ['./display-employees.component.scss']
})
export class DisplayEmployeesComponent  implements OnInit, OnDestroy {

  selectedEmployees: Array<Employee> = new Array<Employee>();
  isGridlistActive: boolean = false;
  user: User;
  employees: Array<Employee> = new Array<Employee>();
  filteredEmployees: Array<Employee>;
  selectedDate: string = null;

  private currentUserSubscription: Subscription;
  private getEmployeeSubscription: Subscription;
  private selectedEmployeesSubscription: Subscription;
  private updateEmployeesSubscription: Subscription;

  constructor(
    private displayEmployeeListService: EmployeesService,
    private authenticationService: ZepSigninService,
    private snackbar: MatSnackBar
  ) {

  }

  ngOnInit() {
    this.currentUserSubscription = this.authenticationService.currentEmployee.subscribe((user: User) => {
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
      this.getEmployeeSubscription = this.displayEmployeeListService.getEmployees()
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
      .filter(empl => empl.firstName.toLowerCase().includes(filterValue.toLowerCase())
        || empl.sureName.toLowerCase().includes(filterValue.toLowerCase()));
  }

  openSnackBar(message: string) {
    this.snackbar.open(message, null, {
      duration: 3000,
    });
  }
}
