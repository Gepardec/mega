import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Employee } from './models/Employee';
import { EmployeeService } from './services/employee.service';
import { SelectionChange } from '@angular/cdk/collections';

@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html'
})
export class EmployeesComponent implements OnInit {

  selectedEmployees: BehaviorSubject<Array<Employee>> = new BehaviorSubject<Array<Employee>>(new Array<Employee>());
  resetSelection: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(
    private employeeService: EmployeeService
  ) {
  }

  getEmployees(): Observable<Array<Employee>> {
    return this.employeeService.getAll();
  }

  updateEmployees(employees: Array<Employee>, date: string) {
    employees.forEach(empl => empl.releaseDate = date);
    return this.employeeService.updateAll(employees);
  }

  setSelectedEmployees(value: SelectionChange<Employee>) {
    const employees: Array<Employee> = this.selectedEmployees.getValue();

    if (value != null) {
      for (const employee of value.added) {
        employees.push(employee);
      }

      for (const empl of value.removed) {
        const index = employees.findIndex(d => d.userId === empl.userId);
        // remove element from array
        employees.splice(index, 1);
      }
    }
    this.selectedEmployees.next(employees);
  }

  setResetSelection(value: boolean) {
    this.resetSelection.next(value);
    this.selectedEmployees.next(new Array<Employee>());
  }

  ngOnInit(): void {
  }
}
