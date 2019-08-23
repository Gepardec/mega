export class ResponseHeaderType {
  private _version: string;
  private _returnCode: string;
  private _message: string;

  get version(): string {
    return this._version;
  }

  get returnCode(): string {
    return this._returnCode;
  }

  get message(): string {
    return this._message;
  }
}
