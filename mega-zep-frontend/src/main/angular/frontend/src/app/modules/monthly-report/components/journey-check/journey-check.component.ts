import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../../models/MonthlyReport';
import { State } from '../../../shared/models/State';

@Component({
  selector: 'app-journey-check',
  templateUrl: './journey-check.component.html',
  styleUrls: ['./journey-check.component.scss']
})
export class JourneyCheckComponent implements OnInit {
  @Input() monthlyReport: MonthlyReport;
  displayedColumns = ['dateJourney', 'warningJourney'];
  State = State;

  constructor() {
  }

  ngOnInit(): void {
  }

  getJourneyWarningString(warnings: Array<string>): string {
    let warningString = '';
    warnings.forEach((value) => warningString += value + '. ');
    return warningString;
  }

}
