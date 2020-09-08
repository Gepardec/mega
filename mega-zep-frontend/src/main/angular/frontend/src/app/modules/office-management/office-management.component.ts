import { Component, OnInit } from '@angular/core';
import { OfficeManagementEntry } from '../employees/models/OfficeManagementEntry';
import { State } from '../shared/models/State';
import { MatDialog } from '@angular/material/dialog';
import { CommentsForEmployeeComponent } from '../shared/components/comments-for-employee/comments-for-employee.component';

@Component({
  selector: 'app-office-management',
  templateUrl: './office-management.component.html',
  styleUrls: ['./office-management.component.scss']
})
export class OfficeManagementComponent implements OnInit {
  State = State;
  states = Object.values(State);
  omEntries = new Array<OfficeManagementEntry>();
  displayedColumns = [
    'name',
    'customerCheckState',
    'internalCheckState',
    'employeeCheckState',
    'projectCheckState',
    'actions',
    'releaseDate'
  ];

  constructor(private dialog: MatDialog) {
    console.log(this.states);
  }

  ngOnInit(): void {
    this.omEntries = [
      {
        employee: {
          firstName: 'Mock',
          sureName: 'Other',
          workDescription: '02',
          email: 'someemail@example.com',
          role: 1,
          salutation: 'Mister',
          title: 'Mag.',
          userId: '000-mother',
          active: true,
          releaseDate: '2020-02-02'
        },
        comments: [
          {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 12, message: 'asdasdasd'},
          {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 15, message: 'asdasdasd'},
          {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 14, message: 'asdasdasd'},
          {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 13, message: 'asdasdasd'}
        ],
        customerCheckState: State.DONE,
        employeeCheckState: State.OPEN,
        internalCheckState: State.OPEN,
        projectCheckState: State.DONE,
        id: 12
      },
    ];
  }

  countOfDone(omEntry: OfficeManagementEntry): number {
    return omEntry.comments.filter(comment => {
      return comment.state === State.DONE;
    }).length;
  }

  openDialog(omEntry: OfficeManagementEntry): void {
    const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
      {
        minWidth: '40%',
        autoFocus: false
      }
    );

    dialogRef.componentInstance.employee = omEntry.employee;
    dialogRef.componentInstance.comments = omEntry.comments;

    dialogRef.afterClosed().subscribe(result => {
      console.log('result: ', result);
    });
  }
}
