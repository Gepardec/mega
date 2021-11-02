import {Injectable} from '@angular/core';
import {configuration} from '../../constants/configuration';
import {LogEntry} from '../../models/LogEntry';

@Injectable({
  providedIn: 'root'
})
export class LoggingService {

  private level: number = configuration.LogLevel.All;
  private logWithDate: boolean = configuration.logWithDate;

  constructor() {
  }

  public writeToLog(msg: string,
                    level: number): void {
    if (this.shouldLog(level)) {
      const entry: LogEntry = new LogEntry();
      entry.message = msg;
      entry.level = level;
      entry.logWithDate = this.logWithDate;
      console.log('test');
      console.log(entry.buildLogString());
      console.log(entry.buildLogString());
    }
  }

  private shouldLog(level: number) {
    let ret = false;
    if ((level >= this.level &&
      level !== configuration.LogLevel.Off) ||
      this.level === configuration.LogLevel.All) {
      ret = true;
    }
    console.log(ret);
    return ret;
  }
}
