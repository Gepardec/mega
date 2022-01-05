import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-billable-times-fraction',
  templateUrl: './billable-times-fraction.component.html',
  styleUrls: ['./billable-times-fraction.component.scss']
})
export class BillableTimesFractionComponent implements OnInit {

  @Input() billableTimes: number;
  @Input() nonBillableTimes: number;

  constructor() {
  }

  ngOnInit(): void {
  }
}
