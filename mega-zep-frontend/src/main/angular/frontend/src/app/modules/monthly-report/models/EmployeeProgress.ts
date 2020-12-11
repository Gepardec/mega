import {State} from "../../shared/models/State";

export class EmployeeProgress {
  state: State;
  project: string;
  stepId: number;
  assigneeEmail: string;
}
