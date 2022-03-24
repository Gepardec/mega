import {State} from "../../shared/models/State";

export interface PmProgress {
  firstname: string;
  lastname: string;
  state: State;
  project: string;
  stepId: number;
  assigneeEmail: string;
}
