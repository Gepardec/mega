import {MitarbeiterType} from "./MitarbeiterType";

export class MitarbeiterListeType {
  private _mitarbeiter: Array<MitarbeiterType>;
  private _length: number;

  get mitarbeiter(): Array<MitarbeiterType> {
    return this._mitarbeiter;
  }

  set mitarbeiter(value: Array<MitarbeiterType>) {
    this._mitarbeiter = value;
  }

  get length(): number {
    return this._length;
  }

  set length(value: number) {
    this._length = value;
  }
}
