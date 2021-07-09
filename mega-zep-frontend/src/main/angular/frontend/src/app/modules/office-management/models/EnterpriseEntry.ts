import {ProjectState} from '../../shared/models/ProjectState';

export class EnterpriseEntry {
  zepTimesReleased: ProjectState;
  chargeabilityExternalEmployeesRecorded: ProjectState;
  payrollAccountingSent: ProjectState;
  zepMonthlyReportDone: ProjectState;
  currentMonthYear: string;

  constructor(zepTimesReleased: ProjectState, chargeabilityExternalEmployeesRecorded: ProjectState, payrollAccountingSent: ProjectState, zepMonthlyReportDone: ProjectState, currentMonthYear: string) {
    this.zepTimesReleased = zepTimesReleased;
    this.chargeabilityExternalEmployeesRecorded = chargeabilityExternalEmployeesRecorded;
    this.payrollAccountingSent = payrollAccountingSent;
    this.zepMonthlyReportDone = zepMonthlyReportDone;
    this.currentMonthYear = currentMonthYear;
  }
}
