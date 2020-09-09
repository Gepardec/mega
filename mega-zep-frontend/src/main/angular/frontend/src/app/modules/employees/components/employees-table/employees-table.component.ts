import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { SelectionChange, SelectionModel } from '@angular/cdk/collections';
import { Subscription } from 'rxjs';
import { configuration } from '../../../shared/constants/configuration';
import { Employee } from '../../../shared/models/Employee';
import { EmployeesService } from '../../services/employees.service';

@Component({
  selector: 'app-employees-table',
  templateUrl: './employees-table.component.html',
  styleUrls: ['./employees-table.component.scss']
})
export class EmployeesTableComponent implements OnInit, OnDestroy {

  @Input() employees: Array<Employee>;
  displayedColumns = ['select', 'name', 'workDescription', 'releaseDate'];
  selection = new SelectionModel<Employee>(true, null);
  readonly currentDate = new Date();
  readonly dayOfMonthForWarning = 5;
  readonly FUNCTIONS = configuration.EMPLOYEE_FUNCTIONS;
  private selectionChangedSubscription: Subscription;

  constructor(
    private displayEmployeeService: EmployeesService
  ) {
  }

  ngOnInit() {
    this.selectionChangedSubscription = this.selection.changed.subscribe(
      (selectedEmployees: SelectionChange<Employee>) => {
        this.displayEmployeeService.setSelectedEmployees(selectedEmployees);
      }
    );

    this.displayEmployeeService.resetSelection.subscribe((resetSelection: boolean) => {
      this.selection.clear();
    });
  }

  ngOnDestroy(): void {
    if (this.selectionChangedSubscription) {
      this.selectionChangedSubscription.unsubscribe();
    }
  }

  isAllSelected() {
    return this.employees && this.selection.selected.length === this.employees.length;
  }

  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.employees.forEach(row => this.selection.select(row));
  }

  checkboxLabel(row?): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.position + 1}`;
  }

  getReleaseDateColourCoding(date: string): string {
    const releaseDate = new Date(date);
    const monthDiff = this.monthDiff(releaseDate, this.currentDate);

    // (previous month || same month || in future) -> OK
    if (monthDiff === 1 || monthDiff === 0 || releaseDate > this.currentDate) {
      return 'done';
    }

    // Two Months ago -> WARN til 5th of month
    if (monthDiff === 2 && this.currentDate.getDate() <= this.dayOfMonthForWarning) {
      return 'wip';
    }

    // everything else -> NOK
    return 'warning';
  }

  // TODO: maybe switch to a library that does this kind of calculations
  private monthDiff(d1: Date, d2: Date) {
    let months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return Math.abs(months);
  }
}
