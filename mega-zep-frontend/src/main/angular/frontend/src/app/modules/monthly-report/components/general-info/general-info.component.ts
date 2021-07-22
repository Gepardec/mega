import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';

const identifiers: string[] = [
  'Arbeitszeit gesamt',
  'fakturierbare Stunden',
  'Verrechenbarkeit',
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

  dataidentifiers: string[];
  billablePercentage: number;

  constructor() {
    this.dataidentifiers = identifiers;
  }

  ngOnInit(): void {
      this.billablePercentage = this.calculateBillingPercentage(this.monthlyReport.totalWorkingTime, this.monthlyReport.billableTime);
  }

  calculateBillingPercentage(totalWorkingTime: string, billableTime: string): number {

    let confTotalWorkingTime = totalWorkingTime.split(":");
    let confTotalWorkingTimeMinutes: number = (Number(confTotalWorkingTime[0]) * 60) + (Number(confTotalWorkingTime[1]));

    let confBillableTime = billableTime.split(":");
    let confBillableTimeMinutes: number = (Number(confBillableTime[0]) * 60) + Number(confBillableTime[1]);

    if (confTotalWorkingTimeMinutes === 0 || confBillableTimeMinutes === 0) {
      return 0;
    }

    return (confBillableTimeMinutes / confTotalWorkingTimeMinutes) * 100;
  }
}
