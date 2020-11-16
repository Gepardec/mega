import {Employee} from './Employee';

export class NewCommentEntry {
  stepId: number;
  employee: Employee;
  comment: string;

  constructor(stepId: number, employee: Employee, comment: string) {
    this.stepId = stepId;
    this.employee = employee;
    this.comment = comment;
  }
}
