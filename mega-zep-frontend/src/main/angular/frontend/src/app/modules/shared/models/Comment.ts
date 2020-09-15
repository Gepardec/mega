export class Comment {
    id: number;
    message: string;
    author: string;
    updateDate: string;
    state: string;
    isEditing ? = false; // used for UI
}
