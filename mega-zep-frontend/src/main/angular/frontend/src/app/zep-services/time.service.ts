import { Injectable } from '@angular/core';
import {configuration} from "../../configuration/configuration";
import {HttpClient} from "@angular/common/http";
import {SocialUser} from "angularx-social-login";
import {TimeEntry} from "../models/MonthlyReport/TimeEntry";
import {Employee} from "../models/Employee/Employee";

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  private URL: string = configuration.BASEURL;

  constructor(
    private http: HttpClient
  ) {
  }

  getAll(user: SocialUser): Array<TimeEntry> {
    let employee = new Employee();
    employee.vorname = "Max";
    employee.nachname = "Mustermann";

    let first = new TimeEntry();
    first.employee = employee;
    first.date = Date();
    first.missingRestTime = 1;
    first.missingBreakTime = 0;
    first.excessWorkTime = 0;
    first.warningMessage = "Test Warning 1";

    let second = new TimeEntry();
    second.employee = employee;
    second.date = Date();
    second.missingRestTime = 0;
    second.missingBreakTime = 2.5;
    second.excessWorkTime = 0;
    second.warningMessage = "";

    let third = new TimeEntry();
    third.employee = employee;
    third.date = Date();
    third.missingRestTime = 0;
    third.missingBreakTime = 0;
    third.excessWorkTime = 12.5;
    third.warningMessage = "Test Warning 3";

    return new Array<TimeEntry>(first, second, third);
  }
}
