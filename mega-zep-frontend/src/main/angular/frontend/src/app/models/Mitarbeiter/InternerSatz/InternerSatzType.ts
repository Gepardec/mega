export class InternerSatzType {
  private _id: number;
  private _userId: string;
  private _satz: number;
  private _satztype: number;
  private _startdatum: string;
  private _bemerkung: string;

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

  get satz(): number {
    return this._satz;
  }

  set satz(value: number) {
    this._satz = value;
  }

  get satztype(): number {
    return this._satztype;
  }

  set satztype(value: number) {
    this._satztype = value;
  }

  get startdatum(): string {
    return this._startdatum;
  }

  set startdatum(value: string) {
    this._startdatum = value;
  }

  get bemerkung(): string {
    return this._bemerkung;
  }

  set bemerkung(value: string) {
    this._bemerkung = value;
  }
}
