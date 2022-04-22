import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-billable-times-fraction',
  templateUrl: './billable-times-fraction.component.html',
  styleUrls: ['./billable-times-fraction.component.scss']
})
export class BillableTimesFractionComponent {

  @Input() billableTimes: number;
  @Input() nonBillableTimes: number;

}
