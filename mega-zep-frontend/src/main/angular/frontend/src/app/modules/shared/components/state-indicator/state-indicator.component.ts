import { Component, Input, OnInit } from '@angular/core';
import { State } from '../../models/State';
import {ProjectState} from '../../models/ProjectState';

@Component({
  selector: 'app-state-indicator',
  templateUrl: './state-indicator.component.html',
  styleUrls: ['./state-indicator.component.scss']
})
// TODO: maybe a new component for project-state has to be created, depends on how the different states should be displayed
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
