export class Comment {
  id: number;
  message: string;
  author: string;
  updateDate: string;
  state: string;
  isEditing?: boolean; // used for UI
}
