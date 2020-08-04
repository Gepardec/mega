import { configuration } from '../constants/configuration';

export class LogEntry {
  message = '';
  level = configuration.LogLevel.Debug;
  logWithDate: boolean = configuration.logWithDate;

  buildLogString(): string {
    let ret = '';

    if (this.logWithDate) {
      ret = new Date() + ' - ';
    }
    ret += 'Type: ' + this.level;
    ret += ' - Message: ' + this.message;

    return ret;
  }
}
