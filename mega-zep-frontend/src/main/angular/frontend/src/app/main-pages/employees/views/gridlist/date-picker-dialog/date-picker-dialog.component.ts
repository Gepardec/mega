import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material";
import {MitarbeiterType} from "../../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Component({
  selector: 'app-date-picker-dialog',
  templateUrl: './date-picker-dialog.component.html',
  styleUrls: ['./date-picker-dialog.component.scss']
})
export class DatePickerDialogComponent implements OnInit {

  employee: MitarbeiterType = null;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data
  ) {
  }

  ngOnInit() {
    this.employee = this.data;
  }

  dateChanged(date: string) {
    console.log(date);
  }

}
