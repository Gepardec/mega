import { Component, OnInit } from '@angular/core';
import { Comment } from '../../models/Comment';
import { State } from '../../models/State';
import { configuration } from '../../constants/configuration';
import {Employee} from '../../models/Employee';
import {CommentService} from '../../services/comment/comment.service';
import {User} from '../../models/User';
import {UserService} from '../../services/user/user.service';
import {Step} from '../../models/Step';

@Component({
  selector: 'app-comments-for-employee',
  templateUrl: './comments-for-employee.component.html',
  styleUrls: ['./comments-for-employee.component.scss']
})
export class CommentsForEmployeeComponent implements OnInit {
  MAXIMUM_LETTERS = 500;
  DATE_FORMAT = configuration.dateFormat;
  State = State;
  employee: Employee;
  user: User;
  comments: Array<Comment>;
  step: Step;
  project = '';

  constructor(private commentService: CommentService, private userService: UserService) {
  }

  ngOnInit(): void {
    this.comments = JSON.parse(JSON.stringify(this.comments || null));
    if (this.comments) {
      this.comments.forEach(comment => comment.isEditing = false);
    }

    this.userService.user.subscribe((user) => {
      this.user = user;
    });
  }

  toggleIsEditing(comment: Comment) {
    comment.isEditing = !comment.isEditing;
  }

  editCommentBtnVisible(comment: Comment) {
    return !comment.isEditing && this.user.email === comment.author && comment.state !== State.DONE;
  }

  parseAnchorTags(plainText): string {
    let replacedText;
    let startingWithHttpHttpsOrFtpPattern;
    let startingWithWwwPattern;
    let emailAddressPattern;

    startingWithHttpHttpsOrFtpPattern = /(\b(https?|ftp):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
    replacedText = plainText.replace(startingWithHttpHttpsOrFtpPattern, '<a href="$1" target="_blank">$1</a>');

    startingWithWwwPattern = /(^|[^\/])(www\.[\S]+(\b|$))/gim;
    replacedText = replacedText.replace(startingWithWwwPattern, '$1<a href="http://$2" target="_blank">$2</a>');

    emailAddressPattern = /(([a-zA-Z0-9\-\_\.])+@[a-zA-Z\_]+?(\.[a-zA-Z]{2,6})+)/gim;
    replacedText = replacedText.replace(emailAddressPattern, '<a href="mailto:$1">$1</a>');

    return replacedText;
  }

  createCommentForEmployee(comment: string): void {
    this.commentService.createNewComment(this.employee, comment, this.user.email, this.step, this.project).subscribe(() => {
      this.commentService.getCommentsForEmployee(this.employee).subscribe((comments: Array<Comment>) => {
        this.comments = comments;
      });
    });
  }

  updateCommentForEmployee(comment: Comment): void {
    this.commentService.updateComment(comment);
  }

  deleteCommentOfEmployee(commentToRemove: Comment): void {
    this.commentService.deleteComment(commentToRemove).subscribe(() => {
      this.comments = this.comments.filter(item => item.id !== commentToRemove.id);
    });
  }
}
