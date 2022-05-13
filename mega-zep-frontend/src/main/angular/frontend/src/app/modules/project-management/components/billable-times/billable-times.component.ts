import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-billable-times',
  templateUrl: './billable-times.component.html',
  styleUrls: ['./billable-times.component.scss']
})
export class BillableTimesComponent {

  @Input() billableTimes: string;
  @Input() nonBillableTimes: string;

  transformTimeToFractionNumber(workingTime: string): number {
    if (!workingTime) {
      return undefined;
    }

    const spWorkingTime: string[] = workingTime.split(':');

    if (spWorkingTime.length < 1) {
      return 0;
    }

    return +(spWorkingTime[0]) + (+(spWorkingTime[1]) / 60);
  }
}
