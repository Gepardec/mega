import {KategorieType} from "./KategorieType";

export class KategorieListeType {
  private _kategorie: Array<KategorieType>;

  get kategorie(): Array<KategorieType> {
    return this._kategorie;
  }

  set kategorie(value: Array<KategorieType>) {
    this._kategorie = value;
  }
}
