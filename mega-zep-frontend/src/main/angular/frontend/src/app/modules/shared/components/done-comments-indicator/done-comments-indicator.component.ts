import { Component, Input, OnInit } from '@angular/core';
import { Comment } from '../../models/Comment';
import { CommentService } from '../../services/comment/comment.service';

@Component({
  selector: 'app-done-comments-indicator',
  templateUrl: './done-comments-indicator.component.html',
  styleUrls: ['./done-comments-indicator.component.scss']
})
export class DoneCommentsIndicatorComponent implements OnInit {
  @Input() comments: Array<Comment>;
  doneCommentsCount: number;

  constructor(private commentService: CommentService) {
  }

  ngOnInit(): void {
    this.doneCommentsCount = this.commentService.getDoneCommentsCount(this.comments);
  }
}
