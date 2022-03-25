import {Component, Input} from '@angular/core';
import {State} from '../../models/State';
import {ProjectState} from '../../models/ProjectState';

@Component({
  selector: 'app-state-indicator',
  templateUrl: './state-indicator.component.html',
  styleUrls: ['./state-indicator.component.scss']
})
export class StateIndicatorComponent {

  State = State;
  ProjectState = ProjectState;

  @Input() state: string;
  @Input() size: 'small' | 'medium' | 'large' = 'small';
}
