export class Comment {
  id: number;
  message: string;
  author: string;
  authorUpdateDate: string;
  state: string;
  stateUpdateDate: string;
  isEditing ? = false; // used for UI
}
