import { Comment } from '../../shared/models/Comment';

export class ProjectManagementEntry {
  employeeName: string;
  projectCheckState: string;
  employeeCheckState: string;
  internalCheckState: string;
  customerCheckState: string;
  comments: Array<Comment>;
}
