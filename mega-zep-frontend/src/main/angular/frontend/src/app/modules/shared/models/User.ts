import { Role } from './Role';

export class User {
  userId: string;
  email: string;
  firstname: string;
  lastname: string;
  roles: Role[];
}
