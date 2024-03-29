import {Employee} from './Employee';
import {State} from './State';
import {PmProgress} from '../../monthly-report/models/PmProgress';

export interface ManagementEntry {

  employee: Employee;
  employeeCheckState: State;
  customerCheckState: State;
  internalCheckState: State;
  projectCheckState: State;
  employeeProgresses: Array<PmProgress>;
  totalComments: number;
  finishedComments: number;
  entryDate: string;
  billableTime: string;
  nonBillableTime: string;
}
