import {Component, Input, OnInit} from '@angular/core';
import {configuration} from "../../../../../shared/constants/configuration";
import {Employee} from "../../../../../shared/models/Employee/Employee";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {DatePickerDialogComponent} from "../../../../../shared/components/date-picker-dialog/date-picker-dialog.component";

@Component({
  selector: 'app-employees-grid-list',
  templateUrl: './employees-grid-list.component.html',
  styleUrls: ['./employees-grid-list.component.scss']
})
export class EmployeesGridListComponent implements OnInit {

  readonly date = new Date();
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  @Input('employees') employees: Array<Employee>;
  @Input('pageSize') pageSize: number;
  @Input('pageIndex') pageIndex: number;

  constructor(
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
  }

  openDialog(employee: Employee): void {
    let config: MatDialogConfig = new MatDialogConfig();
    config.data = employee;
    // TODO: why is DatePickerDialogComponent not imported from SharedModule???
    const dialogRef = this.dialog.open(DatePickerDialogComponent, config);

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog closed ${result}`);
    });
  }

  stringToDate(date: string): Date {
    return new Date(date);
  }

}
