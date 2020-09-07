import { Employee } from '../../employees/models/Employee';
import { TimeWarning } from './TimeWarning';
import { JourneyWarning } from './JourneyWarning';
import { Comment } from './Comment';

export class MonthlyReport {
  comments: Array<Comment>;
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  employeeCheckState: string;
  otherChecksDone: boolean;
  employee: Employee;
}
