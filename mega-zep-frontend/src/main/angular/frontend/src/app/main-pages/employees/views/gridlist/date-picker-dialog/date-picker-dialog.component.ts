import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatSnackBar} from "@angular/material";
import {MitarbeiterType} from "../../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {UtilService} from "../../../../../shared/util/util.service";

@Component({
  selector: 'app-date-picker-dialog',
  templateUrl: './date-picker-dialog.component.html',
  styleUrls: ['./date-picker-dialog.component.scss']
})
export class DatePickerDialogComponent implements OnInit {

  employee: MitarbeiterType = null;
  date: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data,
    private utilService: UtilService,
    private _snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.employee = this.data;
  }

  updateEmployee(employee: MitarbeiterType) {
    let employees: Array<MitarbeiterType> = new Array<MitarbeiterType>(employee);
    this.updateEmployees(employees);
  }

  updateEmployees(employees: Array<MitarbeiterType>) {
    employees.forEach(employee => {
      employee.freigabedatum = this.date;
    });

    this.utilService.updateEmployees(employees).subscribe((response: Response) => {
      this.openSnackBar(response['responseHeader']['message']);
    });
  }

  dateChanged(date: string) {
    this.date = date;
  }

  openSnackBar(message: string) {
    return this._snackBar.open(message, null, {
      duration: 3000,
    });
  }

}
