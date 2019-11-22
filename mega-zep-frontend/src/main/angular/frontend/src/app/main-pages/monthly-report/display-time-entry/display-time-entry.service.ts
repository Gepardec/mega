import { Injectable } from '@angular/core';
import {SocialUser} from "angularx-social-login";
import {TimeEntry} from "../../../models/MonthlyReport/TimeEntry";
import {TimeService} from "../../../zep-services/time.service";

@Injectable({
  providedIn: 'root'
})
export class DisplayTimeEntryService {

  constructor(
    private timeService: TimeService
  ) {
  }

  getAll(user: SocialUser): Array<TimeEntry> {
    return this.timeService.getAll(user);
  }
}
