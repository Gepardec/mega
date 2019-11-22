import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material";
import {Employee} from "../../../../models/Employee/Employee";
import {SelectionChange, SelectionModel} from "@angular/cdk/collections";
import {configuration} from "../../../../../configuration/configuration";
import {Subscription} from "rxjs";
import {DisplayEmployeesService} from "../../display-employees/display-employees.service";

@Component({
  selector: 'app-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.scss']
})
export class TablelistComponent implements OnInit, OnDestroy {

  readonly date = new Date();
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  displayedColumns = ['nachname', 'abteilung', 'freigabedatum', 'auswaehlen'];
  @Input('dataSource') dataSource: MatTableDataSource<Employee>;
  selection = new SelectionModel<Employee>(true, null);

  private selectionChangedSubscription: Subscription;

  constructor(
    private displayEmployeeService: DisplayEmployeesService
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
    this.selectionChangedSubscription && this.selectionChangedSubscription.unsubscribe();
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
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

  stringToDate(date: string): Date {
    return new Date(date);
  }
}
