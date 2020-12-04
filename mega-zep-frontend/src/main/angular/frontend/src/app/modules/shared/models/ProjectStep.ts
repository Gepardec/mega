import {Employee} from './Employee';

export class ProjectStep {

  stepId: number;
  employee: Employee;
  projectName: string;
  currentMonthYear: string;

  constructor(stepId: number, employee: Employee, projectName: string, currentMonthYear: string) {
    this.stepId = stepId;
    this.employee = employee;
    this.projectName = projectName;
    this.currentMonthYear = currentMonthYear;
  }
}
