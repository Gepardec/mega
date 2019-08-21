import {RegelarbeitszeitType} from "./RegelarbeitszeitType";

export class RegelarbeitszeitListeType {
  private _regelarbeitszeit: Array<RegelarbeitszeitType>;

  get regelarbeitszeit(): Array<RegelarbeitszeitType> {
    return this._regelarbeitszeit;
  }

  set regelarbeitszeit(value: Array<RegelarbeitszeitType>) {
    this._regelarbeitszeit = value;
  }
}
