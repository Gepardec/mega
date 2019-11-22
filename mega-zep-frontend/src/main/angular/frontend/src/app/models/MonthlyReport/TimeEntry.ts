import {Employee} from "../Employee/Employee";

export class TimeEntry {
  private _employee: Employee;
  private _date: string;
  private _missingRestTime: number;
  private _missingBreakTime: number;
  private _excessWorkTime: number;
  private _warningMessage: string;

  get employee(): Employee {
    return this._employee;
  }

  set employee(value: Employee) {
    this._employee = value;
  }

  get date(): string {
    return this._date;
  }

  set date(value: string) {
    this._date = value;
  }

  get missingRestTime(): number {
    return this._missingRestTime;
  }

  set missingRestTime(value: number) {
    this._missingRestTime = value;
  }

  get missingBreakTime(): number {
    return this._missingBreakTime;
  }

  set missingBreakTime(value: number) {
    this._missingBreakTime = value;
  }

  get excessWorkTime(): number {
    return this._excessWorkTime;
  }

  set excessWorkTime(value: number) {
    this._excessWorkTime = value;
  }

  get warningMessage(): string {
    return this._warningMessage;
  }

  set warningMessage(value: string) {
    this._warningMessage = value;
  }
}
