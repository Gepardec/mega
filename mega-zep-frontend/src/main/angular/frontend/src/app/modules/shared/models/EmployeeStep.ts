import {Employee} from './Employee';

export class EmployeeStep {

  stepId: number;
  employee: Employee;

  constructor(stepId: number, employee: Employee) {
    this.stepId = stepId;
    this.employee = employee;
  }
}
