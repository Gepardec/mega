import {TimeWarning} from './TimeWarning';
import {JourneyWarning} from './JourneyWarning';
import {Comment} from '../../shared/models/Comment';
import {Employee} from '../../shared/models/Employee';
import {PmProgress} from "./PmProgress";

export interface MonthlyReport {
  comments: Array<Comment>;
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  employeeCheckState: string;
  otherChecksDone: boolean;
  assigned: boolean;
  employee: Employee;
  employeeProgresses: Array<PmProgress>;
  vacationDays: number;
  homeofficeDays: number;
  compensatoryDays: number;
  billableTime: string;
  totalWorkingTime: string;
}
