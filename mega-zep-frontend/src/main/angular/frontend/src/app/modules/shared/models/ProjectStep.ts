import {Employee} from './Employee';

export class ProjectStep {

  stepId: number;
  employee: Employee;
  projectName: string;

  constructor(stepId: number, employee: Employee, projectName: string) {
    this.stepId = stepId;
    this.employee = employee;
    this.projectName = projectName;
  }
}
