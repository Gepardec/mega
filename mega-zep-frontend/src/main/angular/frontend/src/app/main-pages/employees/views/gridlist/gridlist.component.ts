import {Component, Input, OnInit} from '@angular/core';
import {Employee} from "../../../../models/Employee/Employee";
import {configuration} from "../../../../../configuration/configuration";
import {DisplayEmployeesService} from "../../display-employees/display-employees.service";
import {MatDialog, MatDialogConfig} from "@angular/material";
import {DatePickerDialogComponent} from "./date-picker-dialog/date-picker-dialog.component";

@Component({
  selector: 'app-gridlist',
  templateUrl: './gridlist.component.html',
  styleUrls: ['./gridlist.component.scss']
})
export class GridlistComponent implements OnInit {

  readonly date = new Date();
  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  @Input('employees') employees: Array<Employee>;
  @Input('pageSize') pageSize: number;
  @Input('pageIndex') pageIndex: number;


  constructor(
    private displayEmployeeListService: DisplayEmployeesService,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
  }

  openDialog(employee: Employee): void {
    let config: MatDialogConfig = new MatDialogConfig();
    config.data = employee;
    const dialogRef = this.dialog.open(DatePickerDialogComponent, config);


    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog closed ${result}`);
    });
  }

  stringToDate(date: string): Date {
    return new Date(date);
  }


}
