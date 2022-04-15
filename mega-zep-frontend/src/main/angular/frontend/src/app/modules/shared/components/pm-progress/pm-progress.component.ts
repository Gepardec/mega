import {Component, Inject, OnInit} from '@angular/core';
import {PmProgress} from '../../../monthly-report/models/PmProgress';
import {State} from '../../models/State';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material/bottom-sheet';

class DisplayedEmployees {
  name: string;
  state: State;

  constructor(name: string, state: State) {
    this.name = name;
    this.state = state;
  }
}

@Component({
  selector: 'app-employee-progress',
  templateUrl: './pm-progress.component.html',
  styleUrls: ['./pm-progress.component.scss']
})
export class PmProgressComponent implements OnInit {

  pmProgresses: Array<PmProgress>;
  displayedEmployees: Array<DisplayedEmployees>;
  displayedColumns = ['email', 'checked']

  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) public data: any) {
    this.pmProgresses = data.employeeProgresses;
  }

  ngOnInit(): void {
    let map: Map<string, Array<State>> = new Map<string, Array<State>>();
    this.pmProgresses.forEach(pmProgress => {
      let name = pmProgress.firstname + ' ' + pmProgress.lastname;
      if (map.has(name)) {
        map.get(name).push(pmProgress.state);
      } else {
        map.set(name, new Array<State>(pmProgress.state))
      }
    });
    this.displayedEmployees = new Array<DisplayedEmployees>();
    map.forEach(((value, key) => {
      let allDone: boolean = value.every(state => state === State.DONE);
      this.displayedEmployees.push(new DisplayedEmployees(
        key,
        allDone ? State.DONE : State.OPEN
      ));
    }));
  }
}
