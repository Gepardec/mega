export class RegularWorkingTime {
  private _id: number;
  private _userId: string;
  private _startdatum: string;
  private _montag: number;
  private _dienstag: number;
  private _mittwoch: number;
  private _donnerstag: number;
  private _freitag: number;
  private _samstag: number;
  private _sonntag: number;
  private _maximumStundenImMonat: number;
  private _maximumStundenInWoche: number;
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

  get montag(): number {
    return this._montag;
  }

  set montag(value: number) {
    this._montag = value;
  }

  get dienstag(): number {
    return this._dienstag;
  }

  set dienstag(value: number) {
    this._dienstag = value;
  }

  get mittwoch(): number {
    return this._mittwoch;
  }

  set mittwoch(value: number) {
    this._mittwoch = value;
  }

  get donnerstag(): number {
    return this._donnerstag;
  }

  set donnerstag(value: number) {
    this._donnerstag = value;
  }

  get freitag(): number {
    return this._freitag;
  }

  set freitag(value: number) {
    this._freitag = value;
  }

  get samstag(): number {
    return this._samstag;
  }

  set samstag(value: number) {
    this._samstag = value;
  }

  get sonntag(): number {
    return this._sonntag;
  }

  set sonntag(value: number) {
    this._sonntag = value;
  }

  get maximumStundenImMonat(): number {
    return this._maximumStundenImMonat;
  }

  set maximumStundenImMonat(value: number) {
    this._maximumStundenImMonat = value;
  }

  get maximumStundenInWoche(): number {
    return this._maximumStundenInWoche;
  }

  set maximumStundenInWoche(value: number) {
    this._maximumStundenInWoche = value;
  }

  get action(): string {
    return this._action;
  }

  set action(value: string) {
    this._action = value;
  }
}
