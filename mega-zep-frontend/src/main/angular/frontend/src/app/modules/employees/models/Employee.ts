export class Employee {
  private _userId: string;
  private _sureName: string;
  private _firstName: string;
  private _eMail: string;
  private _role: number;
  private _title: string;
  private _releaseDate: string;
  private _workDescription: string;
  private _salutation: string;

  get userId(): string {
    return this._userId;
  }

  set userId(value: string) {
    this._userId = value;
  }

  get sureName(): string {
    return this._sureName;
  }

  set sureName(value: string) {
    this._sureName = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get eMail(): string {
    return this._eMail;
  }

  set eMail(value: string) {
    this._eMail = value;
  }

  get salutation(): string {
    return this._salutation;
  }

  set salutation(value: string) {
    this._salutation = value;
  }

  get workDescription(): string {
    return this._workDescription;
  }

  set workDescription(value: string) {
    this._workDescription = value;
  }

  get releaseDate(): string {
    return this._releaseDate;
  }

  set releaseDate(value: string) {
    this._releaseDate = value;
  }

  get title(): string {
    return this._title;
  }

  set title(value: string) {
    this._title = value;
  }

  get role(): number {
    return this._role;
  }

  set role(value: number) {
    this._role = value;
  }
}
