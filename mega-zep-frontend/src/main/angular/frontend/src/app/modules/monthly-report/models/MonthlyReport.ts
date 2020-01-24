import { Employee } from "../../employees/models/Employee";
import { TimeWarning } from "./TimeWarning";
import { JourneyWarning } from "./JourneyWarning";

export class MonthlyReport {
  timeWarnings: Array<TimeWarning>;
  journeyWarnings: Array<JourneyWarning>;
  employee: Employee;
}
