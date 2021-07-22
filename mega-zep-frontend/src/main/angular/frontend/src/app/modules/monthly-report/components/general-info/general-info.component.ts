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
  convTotalWorkingTimeHours: number;
  convBillableTimeHours: number;

  constructor() {
    this.dataidentifiers = identifiers;
  }

  ngOnInit(): void {
      //this.billablePercentage = this.calculateBillingPercentage(this.monthlyReport.totalWorkingTime, this.monthlyReport.billableTime);
      this.billablePercentage = this.calculateBillingPercentage('50:00', '8:30');
  }

  calculateBillingPercentage(totalWorkingTime: string, billableTime: string): number {
    let spTotalWorkingTime: string[] = totalWorkingTime.split(":");
    let spBillableTime: string[] = billableTime.split(":");

    // if split is not possible return 0
    if(spTotalWorkingTime.length < 1 || spBillableTime.length < 1) {
      return 0;
    }

    let convTotalWorkingTimeMinutes: number = (Number(spTotalWorkingTime[0]) * 60) + (Number(spTotalWorkingTime[1]));
    let convBillableTimeMinutes: number = (Number(spBillableTime[0]) * 60) + Number(spBillableTime[1]);
    this.convTotalWorkingTimeHours = convTotalWorkingTimeMinutes / 60;
    this.convBillableTimeHours = convBillableTimeMinutes / 60;

    // prevent division by zero
    if (convTotalWorkingTimeMinutes === 0 || convBillableTimeMinutes === 0) {
      return 0;
    }

    return (convBillableTimeMinutes / convTotalWorkingTimeMinutes) * 100;
  }
}
