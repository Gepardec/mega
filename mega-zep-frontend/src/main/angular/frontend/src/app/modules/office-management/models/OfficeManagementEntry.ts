import { Employee } from '../../shared/models/Employee';
import { Comment } from '../../shared/models/Comment';

export class OfficeManagementEntry {
  id: number;
  employee: Employee;
  employeeCheckState: string;
  customerCheckState: string;
  internalCheckState: string;
  projectCheckState: string;
  comments: Array<Comment>;
}
