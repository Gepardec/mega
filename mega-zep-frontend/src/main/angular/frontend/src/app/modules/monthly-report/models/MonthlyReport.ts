import { TimeWarning } from './TimeWarning';
import { JourneyWarning } from './JourneyWarning';
import { Comment } from '../../shared/models/Comment';
import { Employee } from '../../shared/models/Employee';

export class MonthlyReport {
  comments: Array<Comment>;
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  employeeCheckState: string;
  otherChecksDone: boolean;
  employee: Employee;
}
