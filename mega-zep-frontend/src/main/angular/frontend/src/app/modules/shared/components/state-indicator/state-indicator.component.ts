import { Component, Input, OnInit } from '@angular/core';
import { State } from '../../models/State';

@Component({
  selector: 'app-state-indicator',
  templateUrl: './state-indicator.component.html',
  styleUrls: ['./state-indicator.component.scss']
})
export class StateIndicatorComponent implements OnInit {
  @Input() state: string;
  @Input() size: 'small' | 'medium' | 'large' = 'small';
  State = State;

  constructor() {
  }

  ngOnInit(): void {
  }

}
