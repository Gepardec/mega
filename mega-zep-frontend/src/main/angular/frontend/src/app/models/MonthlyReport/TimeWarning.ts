
export class TimeWarning {

  private _date: Date;
  private _missingRestTime: number;
  private _missingBreakTime: number;
  private _excessWorkTime: number;
  // FIXME GAJ: what is stored in this array?!?
  private _warnings: Array<String>;

  get date(): Date {
    return this._date;
  }

  set date(value: Date) {
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

  get warnings(): Array<String> {
    return this._warnings;
  }

  set warnings(value: Array<String>) {
    this._warnings = value;
  }
}
