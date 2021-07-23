import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-billable-times',
  templateUrl: './billable-times.component.html',
  styleUrls: ['./billable-times.component.scss']
})
export class BillableTimesComponent implements OnInit {

  @Input() billableTimes: string;
  @Input() nonBillableTimes: string;

  readonly assetsPath = '../../../../../assets/';
  readonly monetizationOff = 'monetization-off.png';

  constructor() {
  }

  ngOnInit(): void {
  }

  transformTimeToFractionNumber(workingTime: string): number {
    let spWorkingTime: string[] = workingTime.split(":");

    if (spWorkingTime.length < 1) {
      return 0;
    }

    return +(spWorkingTime[0]) + (+(spWorkingTime[1]) / 60);
  }
}
