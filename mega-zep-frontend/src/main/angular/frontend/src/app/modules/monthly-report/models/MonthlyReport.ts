import {TimeWarning} from './TimeWarning';
import {JourneyWarning} from './JourneyWarning';
import {Comment} from '../../shared/models/Comment';
import {Employee} from '../../shared/models/Employee';
import {EmployeeProgress} from "./EmployeeProgress";

export class MonthlyReport {
  comments: Array<Comment>;
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  employeeCheckState: string;
  otherChecksDone: boolean;
  assigned: boolean;
  employee: Employee;
  employeeProgresses: Array<EmployeeProgress>;
}
