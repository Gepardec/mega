import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {genInfoData} from "./genInfoData";

const identifiers: string[] = [
  'Arbeitszeit',
  'fakturierbar',
  'Urlaub',
  'Zeitausgleich',
  'Homeoffice'
];


@Component({
  selector: 'app-general-info',
  templateUrl: './general-info.component.html',
  styleUrls: ['./general-info.component.scss']
})


export class GeneralInfoComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();

  dataSource: Array<genInfoData>;
  displayedColumns = ['identifier', 'values'];

  constructor() {
  }

  ngOnInit():
    void {
    this.dataSource = this.createDatasource(this.monthlyReport);
  }


  extractData(monthlyReport: MonthlyReport): string[] {
    return [monthlyReport.totalWorkingTime, monthlyReport.billableTime, monthlyReport.vacationDays.toString(), monthlyReport.compensatoryDays.toString(), monthlyReport.homeofficeDays.toString()];
  }

  createDatasource(monthlyReport: MonthlyReport): Array<genInfoData> {
    let genInfoDataList: Array<genInfoData> = new Array<genInfoData>();
    let monthlyReportData: string[] = this.extractData(monthlyReport);
    for (let i = 0; i < monthlyReportData.length; i++) {
      genInfoDataList.push(new genInfoData(identifiers[i], monthlyReportData[i]));
    }
    return genInfoDataList;
  }

}
