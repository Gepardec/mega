import {BeschaeftigungszeitType} from "./BeschaeftigungszeitType";

export class BeschaeftigungszeitListeType {
  private _beschaeftigungszeit: Array<BeschaeftigungszeitType>;

  get beschaeftigungszeit(): Array<BeschaeftigungszeitType> {
    return this._beschaeftigungszeit;
  }

  set beschaeftigungszeit(value: Array<BeschaeftigungszeitType>) {
    this._beschaeftigungszeit = value;
  }
}
