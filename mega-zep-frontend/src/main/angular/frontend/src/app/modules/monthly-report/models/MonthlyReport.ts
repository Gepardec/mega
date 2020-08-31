import { Employee } from '../../employees/models/Employee';
import { TimeWarning } from './TimeWarning';
import { JourneyWarning } from './JourneyWarning';

export class MonthlyReport {
  comments: Array<Comment>;
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  emc: string;
  employee: Employee;
}

export class Comment {
  message: string;
  author: string;
  creationDate: string;
  state: string;
}
