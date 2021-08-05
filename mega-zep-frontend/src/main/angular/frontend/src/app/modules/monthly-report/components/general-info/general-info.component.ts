import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {MonthlyReportService} from "../../services/monthly-report.service";

@Component({
  selector: 'app-general-info',
  templateUrl: './general-info.component.html',
  styleUrls: ['./general-info.component.scss']
})

export class GeneralInfoComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();


  identifiers: string[] = [
    'Gesamte Arbeitszeit',
    'Fakturierbare Stunden',
    'Chargeability',
    'Urlaub',
    'Zeitausgleich',
    'Homeoffice'
  ];

  months: string[] = [
    'Jänner',
    'Februar',
    'März',
    'April',
    'Mai',
    'Juni',
    'Juli',
    'August',
    'September',
    'Oktober',
    'November',
    'Dezember'
  ];

  constructor(public monthlyReportService: MonthlyReportService) {
  }

  update(monthlyReport: MonthlyReport) {
    this.monthlyReport = monthlyReport;
    this.ngOnInit();
  }

  ngOnInit(): void {
    this.monthlyReportService.billablePercentage = this.calculateBillingPercentage(this.monthlyReport.totalWorkingTime, this.monthlyReport.billableTime);
  }

  calculateBillingPercentage(totalWorkingTime: string, billableTime: string): number {
    let spTotalWorkingTime: string[] = totalWorkingTime.split(":");
    let spBillableTime: string[] = billableTime.split(":");

    // if split is not possible return 0
    if (spTotalWorkingTime.length < 1 || spBillableTime.length < 1) {
      return 0;
    }

    this.monthlyReportService.totalWorkingTimeHours = ((+spTotalWorkingTime[0] * 60) + (+spTotalWorkingTime[1])) / 60;
    this.monthlyReportService.billableTimeHours = ((+spBillableTime[0] * 60) + (+spBillableTime[1])) / 60;

    // prevent division by zero
    if (this.monthlyReportService.totalWorkingTimeHours === 0 || this.monthlyReportService.billableTimeHours === 0) {
      return 0;
    }

    return (this.monthlyReportService.billableTimeHours / this.monthlyReportService.totalWorkingTimeHours) * 100;
  }
}
