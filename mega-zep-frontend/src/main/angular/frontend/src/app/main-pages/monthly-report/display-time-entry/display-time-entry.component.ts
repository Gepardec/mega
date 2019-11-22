import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {Employee} from "../../../models/Employee/Employee";

@Component({
  selector: 'app-display-time-entry',
  templateUrl: './display-time-entry.component.html',
  styleUrls: ['./display-time-entry.component.scss']
})
export class DisplayTimeEntryComponent implements OnInit {

  @Input('dataSource') dataSource: MatTableDataSource<Employee>;
  displayedColumns = ['date', 'restTime', 'breakTime', 'workingTime', 'warning'];

  constructor() {  }

  ngOnInit() {
    // FIXME GAJ: remove
    // this.dataSource.data;
  }
}
