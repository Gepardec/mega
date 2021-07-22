import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {MatTableDataSource} from "@angular/material/table";
import {Observable, of} from "rxjs";

@Component({
  selector: 'app-general-info',
  templateUrl: './general-info.component.html',
  styleUrls: ['./general-info.component.scss']
})
export class GeneralInfoComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();



  monthlyReportDataSource: MatTableDataSource<MonthlyReport>;
  //@ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  //@ViewChild(MatSort, { static: true }) sort: MatSort;


  dataSource: string[];


  displayedColumns = ['identifier', 'values'];

  constructor() {
  }

  ngOnInit(): void {
    //let obs:Observable<MonthlyReport> = of(this.monthlyReport);

  }


  extractData(monthlyReport: MonthlyReport): string[] {
    return [monthlyReport.totalWorkingTime, monthlyReport.billableTime, monthlyReport.vacationDays.toString(), monthlyReport.compensatoryDays.toString(), monthlyReport.homeofficeDays.toString()];
  }




}
