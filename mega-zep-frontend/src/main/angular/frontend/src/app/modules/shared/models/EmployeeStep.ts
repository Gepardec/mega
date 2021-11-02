import {Employee} from './Employee';

export class EmployeeStep {

  stepId: number;
  employee: Employee;
  currentMonthYear: string;

  constructor(stepId: number, employee: Employee, currentMonthYear: string) {
    this.stepId = stepId;
    this.employee = employee;
    this.currentMonthYear = currentMonthYear;
  }
}
