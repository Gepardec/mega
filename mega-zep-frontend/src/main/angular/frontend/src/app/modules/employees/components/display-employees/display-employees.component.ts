import { Component, OnDestroy, OnInit } from '@angular/core';
import { Employee } from "../../models/Employee";
import { Subscription } from "rxjs";
import { EmployeesService } from "../../services/employees.service";
import { NotificationService } from '../../../shared/services/notification/notification.service';

@Component({
  selector: 'app-display-employees',
  templateUrl: './display-employees.component.html',
  styleUrls: ['./display-employees.component.scss']
})
export class DisplayEmployeesComponent implements OnInit, OnDestroy {

  selectedEmployees: Array<Employee> = new Array<Employee>();
  isGridlistActive: boolean = false;
  employees: Array<Employee> = new Array<Employee>();
  filteredEmployees: Array<Employee>;
  selectedDate: string = null;

  private getEmployeeSubscription: Subscription;
  private selectedEmployeesSubscription: Subscription;
  private updateEmployeesSubscription: Subscription;

  constructor(
    private displayEmployeeListService: EmployeesService,
    private notificationService: NotificationService) {

  }

  ngOnInit() {
    this.selectedDate = null;
    this.selectedEmployeesSubscription = this.displayEmployeeListService.selectedEmployees
      .subscribe((selectedEmployees: Array<Employee>) => {
        this.selectedEmployees = selectedEmployees;
      });

    this.getAllEmployees();
  }

  ngOnDestroy(): void {
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
          this.notificationService.showSuccess('Mitarbeiter erfolgreich aktualisiert!');
        });
    }
  }

  getAllEmployees() {
    this.getEmployeeSubscription = this.displayEmployeeListService.getEmployees()
      .subscribe((employee: Array<Employee>) => {
        this.employees = employee;
        this.filteredEmployees = this.employees;
        this.selectedEmployees = new Array<Employee>();
        this.displayEmployeeListService.setSelectedEmployees(null);
        this.displayEmployeeListService.setResetSelection(true);
      });
  }

  dateChanged(date: string) {
    this.selectedDate = date;
  }

  applyFilter(filterValue: string) {
    this.filteredEmployees = this.employees
      .filter(empl => empl.firstName.toLowerCase().includes(filterValue.toLowerCase())
        || empl.sureName.toLowerCase().includes(filterValue.toLowerCase()));
  }
}
