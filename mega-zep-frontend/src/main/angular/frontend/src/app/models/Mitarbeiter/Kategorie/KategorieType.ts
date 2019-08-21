export class KategorieType {
  private _kurzform: string;
  private _bezeichnungDe: string;
  private _bezeichnungEn: string;
  private _bezeichnungFr: string;
  private _action: string;

  get kurzform(): string {
    return this._kurzform;
  }

  set kurzform(value: string) {
    this._kurzform = value;
  }

  get bezeichnungDe(): string {
    return this._bezeichnungDe;
  }

  set bezeichnungDe(value: string) {
    this._bezeichnungDe = value;
  }

  get bezeichnungEn(): string {
    return this._bezeichnungEn;
  }

  set bezeichnungEn(value: string) {
    this._bezeichnungEn = value;
  }

  get bezeichnungFr(): string {
    return this._bezeichnungFr;
  }

  set bezeichnungFr(value: string) {
    this._bezeichnungFr = value;
  }

  get action(): string {
    return this._action;
  }

  set action(value: string) {
    this._action = value;
  }
}
