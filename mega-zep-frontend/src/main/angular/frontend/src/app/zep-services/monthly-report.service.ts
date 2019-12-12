import {Injectable} from '@angular/core';
import {configuration} from "../../configuration/configuration";
import {HttpClient} from "@angular/common/http";
import {SocialUser} from "angularx-social-login";
import {MonthlyReport} from "../models/MonthlyReport/MonthlyReport";
import {Employee} from "../models/Employee/Employee";
import {TimeWarning} from "../models/MonthlyReport/TimeWarning";
import {JourneyWarning} from "../models/MonthlyReport/JourneyWarning";
import {retry} from "rxjs/operators";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportService {

  private URL: string = configuration.BASEURL;

  constructor(
    private http: HttpClient
  ) {
  }

  getAll(user: SocialUser): Observable<MonthlyReport> {
    return this.http.post<MonthlyReport>(this.URL +
      '/worker/employee/monthendReport', JSON.stringify(user))
      .pipe(
        retry(1)
      );
  }

  mockService(): MonthlyReport {
    let employee = new Employee();
    employee.vorname = "Max";
    employee.nachname = "Mustermann";
    employee.preisgruppe = "02";
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

    let firstJourneyWarning = new JourneyWarning();
    firstJourneyWarning.date = new Date();
    firstJourneyWarning.warnings = new Array<String>("Test Warning 1", "Test Warning 2", "Test Warning 3");

    let secondJourneyWarning = new JourneyWarning();
    secondJourneyWarning.date = new Date();
    secondJourneyWarning.warnings = new Array<String>("Test Warning 4");

    report.journeyWarnings = new Array<JourneyWarning>(firstJourneyWarning, secondJourneyWarning);
    report.timeWarnings = new Array<TimeWarning>(firstTimeWarning, secondTimeWarning, thirdTimeWarning, thirdTimeWarning, thirdTimeWarning, thirdTimeWarning, thirdTimeWarning, thirdTimeWarning, thirdTimeWarning);

    return report;
  }
}
