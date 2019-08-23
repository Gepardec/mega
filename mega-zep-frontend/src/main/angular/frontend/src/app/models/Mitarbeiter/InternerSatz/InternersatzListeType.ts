import {InternerSatzType} from "./InternerSatzType";

export class InternersatzListeType {
  private _internersatz: Array<InternerSatzType>;

  get internersatz(): Array<InternerSatzType> {
    return this._internersatz;
  }

  set internersatz(value: Array<InternerSatzType>) {
    this._internersatz = value;
  }
}
