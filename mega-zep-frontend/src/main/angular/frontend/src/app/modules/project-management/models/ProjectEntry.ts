import {ProjectState} from '../../shared/models/ProjectState';

export type ProjectStepQualifier = 'CONTROL_PROJECT' | 'CONTROL_BILLING' | 'CREATE_COMPANY_CONTROLLING';

export class ProjectEntry {

  state: ProjectState;
  preset: boolean;
  projectName: string;
  step: ProjectStepQualifier;
  currentMonthYear: string;

  constructor(state: ProjectState, preset: boolean, projectName: string, step: ProjectStepQualifier, currentMonthYear: string) {
    this.state = state;
    this.preset = preset;
    this.projectName = projectName;
    this.step = step;
    this.currentMonthYear = currentMonthYear;
  }
}
