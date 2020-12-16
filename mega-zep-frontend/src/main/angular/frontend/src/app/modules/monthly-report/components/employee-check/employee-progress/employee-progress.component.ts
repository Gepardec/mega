import {Component, Inject, Input, OnInit} from '@angular/core';
import {EmployeeProgress} from "../../../models/EmployeeProgress";
import {State} from "../../../../shared/models/State";
import {MAT_BOTTOM_SHEET_DATA} from "@angular/material/bottom-sheet";

class DisplayedEmployees {
  email: string;
  state: State;

  constructor(email: string, state: State) {
    this.email = email;
    this.state = state;
  }
}

@Component({
  selector: 'app-employee-progress',
  templateUrl: './employee-progress.component.html',
  styleUrls: ['./employee-progress.component.scss']
})
export class EmployeeProgressComponent implements OnInit {


  employeeProgresses: Array<EmployeeProgress>;

  displayedEmployees: Array<DisplayedEmployees>;

  displayedColumns = ['email', 'checked']

  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) public data: any) {
    this.employeeProgresses = data.employeeProgresses;
  }

  ngOnInit(): void {
    let map: Map<string, Array<State>> = new Map<string, Array<State>>();
    this.employeeProgresses.forEach(employeeProgress => {
      if (map.has(employeeProgress.assigneeEmail)) {
        map.get(employeeProgress.assigneeEmail).push(employeeProgress.state);
      } else {
        map.set(employeeProgress.assigneeEmail, new Array<State>(employeeProgress.state))
      }
    })
    this.displayedEmployees = new Array<DisplayedEmployees>();
    map.forEach(((value, key) => {
      let allDone: boolean = value.every(state => state === State.DONE);
      this.displayedEmployees.push(new DisplayedEmployees(
        key,
        allDone ? State.DONE : State.OPEN
      ))
    }))
  }

  getFullNameFromEmail(email: string): string {
    if (!email) {
      return;
    }
    return email.split('@')[0]
      .split('.')
      .map(name => name.charAt(0).toUpperCase() + name.slice(1))
      .join(' ');
  }
}
