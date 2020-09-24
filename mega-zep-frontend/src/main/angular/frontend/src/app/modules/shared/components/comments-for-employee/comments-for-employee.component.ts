import { Component, OnInit } from '@angular/core';
import { Employee } from '../../models/Employee';
import { Comment } from '../../models/Comment';
import { State } from '../../models/State';

@Component({
  selector: 'app-comments-for-employee',
  templateUrl: './comments-for-employee.component.html',
  styleUrls: ['./comments-for-employee.component.scss']
})
export class CommentsForEmployeeComponent implements OnInit {
  MAXIMUM_LETTERS = 500;
  State = State;
  employee: Employee;
  comments: Array<Comment>;

  constructor() {
  }

  ngOnInit(): void {
    this.employee = JSON.parse(JSON.stringify(this.employee || null));
    this.comments = JSON.parse(JSON.stringify(this.comments || null));
    if (this.comments) {
      this.comments.forEach(comment => comment.isEditing = false);
    }
    console.log(this.comments);
  }

  toggleIsEditing(comment: Comment) {
    comment.isEditing = !comment.isEditing;
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
}
