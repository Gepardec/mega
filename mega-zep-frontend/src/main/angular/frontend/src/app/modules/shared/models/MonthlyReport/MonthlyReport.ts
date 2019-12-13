import {Employee} from "../Employee/Employee";
import {TimeWarning} from "./TimeWarning";
import {JourneyWarning} from "./JourneyWarning";

export class MonthlyReport {

  private _timeWarnings: Array<TimeWarning>;
  private _journeyWarnings: Array<JourneyWarning>;
  private _employee: Employee;

  get timeWarnings(): Array<TimeWarning> {
    return this._timeWarnings;
  }

  set timeWarnings(value: Array<TimeWarning>) {
    this._timeWarnings = value;
  }

  get journeyWarnings(): Array<JourneyWarning> {
    return this._journeyWarnings;
  }

  set journeyWarnings(value: Array<JourneyWarning>) {
    this._journeyWarnings = value;
  }

  get employee(): Employee {
    return this._employee;
  }

  set employee(value: Employee) {
    this._employee = value;
  }
}
