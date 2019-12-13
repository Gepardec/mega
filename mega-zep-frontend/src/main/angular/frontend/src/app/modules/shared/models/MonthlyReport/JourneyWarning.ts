
export class JourneyWarning {

  private _date: Date;
  private _warnings: Array<String>;

  get date(): Date {
    return this._date;
  }

  set date(value: Date) {
    this._date = value;
  }

  get warnings(): Array<String> {
    return this._warnings;
  }

  set warnings(value: Array<String>) {
    this._warnings = value;
  }
}
