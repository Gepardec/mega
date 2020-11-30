import {Component, Input} from '@angular/core';
import {CommentService} from '../../services/comment/comment.service';

@Component({
  selector: 'app-done-comments-indicator',
  templateUrl: './done-comments-indicator.component.html',
  styleUrls: ['./done-comments-indicator.component.scss']
})
export class DoneCommentsIndicatorComponent {
  @Input() totalComments: number;
  @Input() finishedComments: number;

  constructor(private commentService: CommentService) {
  }
}
