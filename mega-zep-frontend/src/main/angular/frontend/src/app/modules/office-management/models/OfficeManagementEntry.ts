import {Employee} from "../../shared/models/Employee";
import {State} from "../../shared/models/State";

export class OfficeManagementEntry {
  employee: Employee;
  employeeCheckState: State;
  customerCheckState: State;
  internalCheckState: State;
  projectCheckState: State;
  totalComments: number;
  finishedComments: number;
}
