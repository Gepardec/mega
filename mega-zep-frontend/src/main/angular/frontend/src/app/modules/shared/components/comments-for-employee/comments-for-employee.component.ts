import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Comment} from '../../models/Comment';
import {State} from '../../models/State';
import {Employee} from '../../models/Employee';
import {CommentService} from '../../services/comment/comment.service';
import {User} from '../../models/User';
import {UserService} from '../../services/user/user.service';
import {Step} from '../../models/Step';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {ConfirmDialogComponent} from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-comments-for-employee',
  templateUrl: './comments-for-employee.component.html',
  styleUrls: ['./comments-for-employee.component.scss']
})
export class CommentsForEmployeeComponent implements OnInit {

  @Output() commentHasChanged: EventEmitter<void> = new EventEmitter<void>();
  @ViewChild('newMessage') newCommentTextarea;

  State = State;

  MAXIMUM_LETTERS: number = 500;

  employee: Employee;
  user: User;
  comments: Array<Comment>;
  step: Step;
  project: string = '';
  currentMonthYear: string;

  constructor(private commentService: CommentService,
              private userService: UserService,
              public dialogRef: MatDialogRef<CommentsForEmployeeComponent>,
              public dialog: MatDialog) {
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

  toggleIsEditing(comment: Comment): void {
    comment.isEditing = !comment.isEditing;
  }

  editCommentBtnVisible(comment: Comment): boolean {
    return !comment.isEditing && this.user.email === comment.authorEmail && comment.state !== State.DONE;
  }

  isReleased(date: string): boolean {
    const today = new Date();
    const releaseDate = new Date(date);
    const monthDiff = this.monthDiff(releaseDate, today);
    return (monthDiff === 1 || monthDiff === 0 || releaseDate > today);
  }

  parseAnchorTags(plainText): string {
    let startingWithHttpHttpsOrFtpPattern = /(\b(https?|ftp):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/gim;
    let replacedText = plainText.replace(startingWithHttpHttpsOrFtpPattern, '<a href="$1" target="_blank">$1</a>');

    let startingWithWwwPattern = /(^|[^\/])(www\.[\S]+(\b|$))/gim;
    replacedText = replacedText.replace(startingWithWwwPattern, '$1<a href="http://$2" target="_blank">$2</a>');

    let emailAddressPattern = /(([a-zA-Z0-9\-\_\.])+@[a-zA-Z\_]+?(\.[a-zA-Z]{2,6})+)/gim;
    replacedText = replacedText.replace(emailAddressPattern, '<a href="mailto:$1">$1</a>');

    return replacedText;
  }

  createCommentForEmployee(comment: string): void {
    this.commentService
      .createNewComment(this.employee, comment, this.user.email, this.step, this.project, this.currentMonthYear)
      .subscribe(() => {
        this.commentHasChanged.emit();
        this.commentService.getCommentsForEmployee(this.employee.email, this.currentMonthYear).subscribe((comments: Array<Comment>) => {
          this.comments = comments;
        });
      });
  }

  updateCommentForEmployee(comment: Comment): void {
    this.commentService.updateComment(comment).subscribe(() => {
      this.commentHasChanged.emit();
    });
  }

  deleteCommentOfEmployee(commentToRemove: Comment): void {
    this.commentService.deleteComment(commentToRemove).subscribe(() => {
      this.comments = this.comments.filter(item => item.id !== commentToRemove.id);
    });
    this.commentHasChanged.emit();
  }

  close(): void {
    if (this.newCommentTextarea?.nativeElement.value !== '') {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {});

      dialogRef.afterClosed().subscribe(dialogResult => {
        if (dialogResult === true) {
          this.dialogRef.close(dialogResult);
        }
      });
    } else {
      this.dialogRef.close(true);
    }
  }

  private monthDiff(d1: Date, d2: Date) {
    let months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return Math.abs(months);
  }
}
