import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';

@Component({
  selector: 'app-general-info',
  templateUrl: './general-info.component.html',
  styleUrls: ['./general-info.component.scss']
})

export class GeneralInfoComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();

  billablePercentage: number;
  totalWorkingTimeHours: number;
  billableTimeHours: number;

  identifiers: string[] = [
    'Gesamte Arbeitszeit',
    'Fakturierbare Stunden',
    'Chargeability',
    'Urlaub',
    'Zeitausgleich',
    'Homeoffice'
  ];

  constructor() {
  }

  ngOnInit(): void {
    this.billablePercentage = this.calculateBillingPercentage(this.monthlyReport.totalWorkingTime, this.monthlyReport.billableTime);
  }

  calculateBillingPercentage(totalWorkingTime: string, billableTime: string): number {
    let spTotalWorkingTime: string[] = totalWorkingTime.split(":");
    let spBillableTime: string[] = billableTime.split(":");

    // if split is not possible return 0
    if (spTotalWorkingTime.length < 1 || spBillableTime.length < 1) {
      return 0;
    }

    this.totalWorkingTimeHours = ((+spTotalWorkingTime[0] * 60) + (+spTotalWorkingTime[1])) / 60;
    this.billableTimeHours = ((+spBillableTime[0] * 60) + (+spBillableTime[1])) / 60;

    // prevent division by zero
    if (this.totalWorkingTimeHours === 0 || this.billableTimeHours === 0) {
      return 0;
    }

    return (this.billableTimeHours / this.totalWorkingTimeHours) * 100;
  }
}
