export class Attribute {
  private _value: string;
  private _name: string;

  get value(): string {
    return this._value;
  }

  set value(value: string) {
    this._value = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }
}
