import {Employee} from './Employee';

export class NewCommentEntry {
  stepId: number;
  employee: Employee;
  comment: string;
  assigneeEmail: string;
  project: string;
  currentMonthYear: string;

  constructor(stepId: number, employee: Employee, comment: string, assigneEmail: string, project: string, currentMonthYear: string) {
    this.stepId = stepId;
    this.employee = employee;
    this.comment = comment;
    this.assigneeEmail = assigneEmail;
    this.project = project;
    this.currentMonthYear = currentMonthYear;
  }
}
