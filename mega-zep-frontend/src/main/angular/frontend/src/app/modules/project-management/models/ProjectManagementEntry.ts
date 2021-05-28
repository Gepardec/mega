import {ManagementEntry} from '../../shared/models/ManagementEntry';
import {ProjectState} from "../../shared/models/ProjectState";

export class ProjectManagementEntry {

  projectName: string;
  entries: Array<ManagementEntry>;
  controlProjectState: ProjectState;
  controlBillingState: ProjectState;
  presetControlProjectState: boolean;
  presetControlBillingState: boolean;
}
