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
  State = State;
  displayedColumns = ['state', 'message', 'edit', 'delete'];
  employee: Employee;
  comments: Array<Comment>;

  constructor() {
  }

  ngOnInit(): void {
    this.employee = JSON.parse(JSON.stringify(this.employee));
    this.comments = JSON.parse(JSON.stringify(this.comments));
  }

  toggleEditable(comment: any) {
    comment.edit = !comment.edit;
  }

  setMessage(comment: any, value: any) {

  }

  hi($event: any) {
    console.log('hi');
  }
}
