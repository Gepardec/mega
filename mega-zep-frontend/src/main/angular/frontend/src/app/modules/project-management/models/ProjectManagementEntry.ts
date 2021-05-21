import {ManagementEntry} from '../../shared/models/ManagementEntry';
import {ProjectState} from "../../shared/models/ProjectState";

export class ProjectManagementEntry {

  projectName: string;
  entries: Array<ManagementEntry>;
  projectControllingState: ProjectState;
  projectBillingState: ProjectState;
  presetProjectControllingState: boolean;
  presetBillingState: boolean;
}
