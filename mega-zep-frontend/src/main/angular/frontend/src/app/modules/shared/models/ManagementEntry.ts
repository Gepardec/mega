import {Employee} from './Employee';
import {State} from './State';

export class ManagementEntry {
  employee: Employee;
  employeeCheckState: State;
  customerCheckState: State;
  internalCheckState: State;
  projectCheckState: State;
  totalComments: number;
  finishedComments: number;
}
