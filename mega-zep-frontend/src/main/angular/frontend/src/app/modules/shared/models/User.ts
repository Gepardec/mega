import {Role} from './Role';

export interface User {
  userId: string;
  email: string;
  firstname: string;
  lastname: string;
  roles: Role[];
}
