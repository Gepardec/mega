import {ResponseHeaderType} from "../ResponseHeaderType";
import {MitarbeiterType} from "./Mitarbeiter/MitarbeiterType";

export class MitarbeiterResponseType {
  private _responseHeader: ResponseHeaderType;
  private _length: number;
  private _mitarbeiterTypeList: Array<MitarbeiterType>;

  get responseHeader(): ResponseHeaderType {
    return this._responseHeader;
  }

  set responseHeader(value: ResponseHeaderType) {
    this._responseHeader = value;
  }

  get length(): number {
    return this._length;
  }

  set length(value: number) {
    this._length = value;
  }

  get mitarbeiterTypeList(): Array<MitarbeiterType> {
    return this._mitarbeiterTypeList;
  }

  set mitarbeiterTypeList(value: Array<MitarbeiterType>) {
    this._mitarbeiterTypeList = value;
  }
}
