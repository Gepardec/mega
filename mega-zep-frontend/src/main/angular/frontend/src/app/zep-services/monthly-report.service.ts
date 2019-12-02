import { Injectable } from '@angular/core';
import {configuration} from "../../configuration/configuration";
import {HttpClient} from "@angular/common/http";
import {SocialUser} from "angularx-social-login";
import {MonthlyReport} from "../models/MonthlyReport/MonthlyReport";
import {Employee} from "../models/Employee/Employee";
import {TimeWarning} from "../models/MonthlyReport/TimeWarning";
import {JourneyWarning} from "../models/MonthlyReport/JourneyWarning";

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportService {

  private URL: string = configuration.BASEURL;

  constructor(
    private http: HttpClient
  ) {
  }

  getAll(user: SocialUser): MonthlyReport {
    let employee = new Employee();
    employee.vorname = "Max";
    employee.nachname = "Mustermann";
    employee.freigabedatum = new Date().toDateString();

    let report = new MonthlyReport();
    report.employee = employee;

    let firstTimeWarning = new TimeWarning();
    firstTimeWarning.date = new Date();
    firstTimeWarning.missingRestTime = 1;

    let secondTimeWarning = new TimeWarning();
    secondTimeWarning.date = new Date();
    secondTimeWarning.missingBreakTime = 0.5;

    let thirdTimeWarning = new TimeWarning();
    thirdTimeWarning.date = new Date();
    thirdTimeWarning.excessWorkTime = 10;

    let fourthTimeWarning = new TimeWarning();
    fourthTimeWarning.date = new Date();
    fourthTimeWarning.excessWorkTime = 10;

    let fifthTimeWarning = new TimeWarning();
    fifthTimeWarning.date = new Date();
    fifthTimeWarning.excessWorkTime = 10;

    let sixthTimeWarning = new TimeWarning();
    sixthTimeWarning.date = new Date();
    sixthTimeWarning.excessWorkTime = 10;

    let firstJourneyWarning = new JourneyWarning();
    firstJourneyWarning.date = new Date();
    firstJourneyWarning.warnings = new Array<String>("Test Warning 1", "Test Warning 2", "Test Warning 3");

    let secondJourneyWarning = new JourneyWarning();
    secondJourneyWarning.date = new Date();
    secondJourneyWarning.warnings = new Array<String>("Test Warning 4");

    report.journeyWarnings = new Array<JourneyWarning>(firstJourneyWarning, secondJourneyWarning);
    report.timeWarnings = new Array<TimeWarning>(firstTimeWarning, secondTimeWarning, thirdTimeWarning, fourthTimeWarning, fifthTimeWarning, sixthTimeWarning);

    return report;
  }
}
