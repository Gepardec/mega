import { Injectable } from '@angular/core';
import { Comment } from '../../models/Comment';
import { State } from '../../models/State';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor() {
  }

  getDoneCommentsCount(comments: Array<Comment>): number {
    return this.getDoneComments(comments).length;
  }

  getDoneComments(comments: Array<Comment>): Array<Comment> {
    return comments.filter(comment => comment.state === State.DONE);
  }

  areAllCommentsDone(comments: Array<Comment>): boolean {
    return this.getDoneCommentsCount(comments) === comments.length;
  }
}
