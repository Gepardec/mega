import { Injectable } from '@angular/core';
import {TimeService} from "../../zep-services/time.service";
import {SocialUser} from "angularx-social-login";
import {TimeEntry} from "../../models/MonthlyReport/TimeEntry";

@Injectable({
  providedIn: 'root'
})
export class TimeEntryService {

  constructor(
    private timeService: TimeService
  ) {
  }

  getAll(user: SocialUser): Array<TimeEntry> {
    return this.timeService.getAll(user);
  }
}
