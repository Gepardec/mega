export class BeschaeftigungszeitType {
  private _id: number;
  private _userId: string;
  private _startdatum: string;
  private _enddatum: string;
  private _bemerkung: string;
  private _action: string;

  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }

  get userId(): string {
    return this._userId;
  }

  set userId(value: string) {
    this._userId = value;
  }

  get startdatum(): string {
    return this._startdatum;
  }

  set startdatum(value: string) {
    this._startdatum = value;
  }

  get enddatum(): string {
    return this._enddatum;
  }

  set enddatum(value: string) {
    this._enddatum = value;
  }

  get bemerkung(): string {
    return this._bemerkung;
  }

  set bemerkung(value: string) {
    this._bemerkung = value;
  }

  get action(): string {
    return this._action;
  }

  set action(value: string) {
    this._action = value;
  }
}
