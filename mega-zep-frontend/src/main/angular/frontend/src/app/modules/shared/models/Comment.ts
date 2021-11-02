export class Comment {
  id: number;
  message: string;
  authorEmail: string;
  authorName: string;
  updateDate: string;
  state: string;
  isEditing?: boolean; // used for UI
}
