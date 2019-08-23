import {ResponseHeaderType} from "../ResponseHeaderType";
import {MitarbeiterListeType} from "./Mitarbeiter/MitarbeiterListeType";

export class MitarbeiterResponseType {
  private _responseHeader: ResponseHeaderType;
  private _mitarbeiterListe: MitarbeiterListeType;

  get responseHeader(): ResponseHeaderType {
    return this._responseHeader;
  }

  set responseHeader(value: ResponseHeaderType) {
    this._responseHeader = value;
  }

  get mitarbeiterListe(): MitarbeiterListeType {
    return this._mitarbeiterListe;
  }

  set mitarbeiterListe(value: MitarbeiterListeType) {
    this._mitarbeiterListe = value;
  }
}
