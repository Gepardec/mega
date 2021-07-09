import { Component, Input, OnInit } from '@angular/core';
import { State } from '../../models/State';
import {ProjectState} from '../../models/ProjectState';

@Component({
  selector: 'app-state-indicator',
  templateUrl: './state-indicator.component.html',
  styleUrls: ['./state-indicator.component.scss']
})
export class StateIndicatorComponent implements OnInit {
  @Input() state: string;
  @Input() size: 'small' | 'medium' | 'large' = 'small';
  State = State;
  ProjectState = ProjectState;

  constructor() {
  }

  ngOnInit(): void {
  }

}
