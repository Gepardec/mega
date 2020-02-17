import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { configuration } from '../../../shared/constants/configuration';
import { MatTableDataSource } from '@angular/material/table';
import { Employee } from '../../models/Employee';
import { SelectionChange, SelectionModel } from '@angular/cdk/collections';
import { Subscription } from 'rxjs';
import { EmployeesService } from '../../services/employees.service';

@Component({
  selector: 'app-employees-table-list',
  templateUrl: './employees-table-list.component.html',
  styleUrls: ['./employees-table-list.component.scss']
})
export class EmployeesTableListComponent implements OnInit, OnDestroy {

  releaseDateColourTypes = ['releaseDone', 'releaseInProgress', 'releaseWarning'];
  readonly currentDate = new Date();
  readonly oneMonthAgo = this.currentDate.getMonth() == 0 ? 11 : this.currentDate.getMonth() - 1;
  readonly twoMonthsAgo = this.oneMonthAgo == 0 ? 11 : this.oneMonthAgo - 1;
  readonly dayOfMonthForWarning = 5;

  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  @Input() dataSource: MatTableDataSource<Employee>;
  displayedColumns = ['auswaehlen', 'nachname', 'abteilung', 'freigabedatum'];
  selection = new SelectionModel<Employee>(true, null);

  private selectionChangedSubscription: Subscription;

  constructor(
    private displayEmployeeService: EmployeesService
  ) {
  }

  ngOnInit() {
    // TODO: fix this code(service logic is now in container - how to refactor)
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
    if (this.dataSource) {
      const numSelected = this.selection.selected.length;
      const numRows = this.dataSource.data.length;
      return numSelected === numRows;
    } else {
      return false;
    }
  }

  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  checkboxLabel(row?): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.position + 1}`;
  }

  getReleaseDateColourCoding(date: string): string {

    let releaseDate = new Date(date);

    // previous month -> OK || in future -> OK
    if(releaseDate.getMonth() === this.oneMonthAgo || releaseDate > this.currentDate) {
      return this.releaseDateColourTypes[0];
    }

    // Two Months ago -> WARN til 5th of month
    if(releaseDate.getMonth() === this.twoMonthsAgo && this.currentDate.getDate() <= this.dayOfMonthForWarning) {
      return this.releaseDateColourTypes[1];
    }

    // everything else -> NOK
    return this.releaseDateColourTypes[2];
  }
}
