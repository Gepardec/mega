import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {configuration} from "../../../shared/constants/configuration";
import {MatTableDataSource} from "@angular/material/table";
import {Employee} from "../../models/Employee";
import {SelectionChange, SelectionModel} from "@angular/cdk/collections";
import {Subscription} from "rxjs";
import {EmployeesService} from "../../services/employees.service";

@Component({
  selector: 'app-employees-table-list',
  templateUrl: './employees-table-list.component.html',
  styleUrls: ['./employees-table-list.component.scss']
})
export class EmployeesTableListComponent implements OnInit, OnDestroy {

  readonly date = new Date();
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  displayedColumns = ['nachname', 'abteilung', 'freigabedatum', 'auswaehlen'];
  @Input('dataSource') dataSource: MatTableDataSource<Employee>;
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
