export class Config {
  private _oauthClientId: string;

  get oauthClientId(): string {
    return this._oauthClientId;
  }

  set oauthClientId(value: string) {
    this._oauthClientId = value;
  }
}
