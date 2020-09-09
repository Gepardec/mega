import { Component, OnInit } from '@angular/core';
import { OfficeManagementEntry } from '../models/OfficeManagementEntry';
import { State } from '../../shared/models/State';
import { MatDialog } from '@angular/material/dialog';
import { CommentsForEmployeeComponent } from '../../shared/components/comments-for-employee/comments-for-employee.component';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
  selector: 'app-office-management',
  templateUrl: './office-management.component.html',
  styleUrls: ['./office-management.component.scss']
})
export class OfficeManagementComponent implements OnInit {
  displayedColumns = [
    'select',
    'name',
    'customerCheckState',
    'internalCheckState',
    'employeeCheckState',
    'projectCheckState',
    'actions',
    'releaseDate'
  ];
  State = State;
  states = Object.values(State);
  omEntries = [
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
      comments: [],
      customerCheckState: State.OPEN,
      employeeCheckState: State.DONE,
      internalCheckState: State.OPEN,
      projectCheckState: State.DONE,
      id: 1
    },
    {
      employee: {
        firstName: 'Lincoln',
        sureName: 'Burrows',
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
      id: 2
    },
    {
      employee: {
        firstName: 'Italy',
        sureName: 'Spritzer',
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
      id: 3
    },
    {
      employee: {
        firstName: 'Carlos',
        sureName: 'Has no car',
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
      id: 4
    },
    {
      employee: {
        firstName: 'Pablo',
        sureName: 'Escobar',
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
        {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 14, message: 'asdasdasd'},
        {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 13, message: 'asdasdasd'}
      ],
      customerCheckState: State.OPEN,
      employeeCheckState: State.DONE,
      internalCheckState: State.OPEN,
      projectCheckState: State.DONE,
      id: 5
    },
    {
      employee: {
        firstName: 'Richard',
        sureName: 'Stallman',
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
        {author: 'jeff', state: State.OPEN, creationDate: '2020-02-02', id: 12, message: 'asdasdasd'},
        {author: 'jeff', state: State.OPEN, creationDate: '2020-02-02', id: 15, message: 'asdasdasd'},
        {author: 'jeff', state: State.OPEN, creationDate: '2020-02-02', id: 14, message: 'asdasdasd'},
        {author: 'jeff', state: State.DONE, creationDate: '2020-02-02', id: 13, message: 'asdasdasd'}
      ],
      customerCheckState: State.DONE,
      employeeCheckState: State.OPEN,
      internalCheckState: State.OPEN,
      projectCheckState: State.DONE,
      id: 6
    }
  ];
  filteredOmEntries = new Array<OfficeManagementEntry>();
  // selectedOmEntries = new Array<OfficeManagementEntry>();
  omSelectionModel = new SelectionModel<OfficeManagementEntry>(true, []);
  selectedDate: string = null;

  constructor(private dialog: MatDialog) {
    console.log(this.states);
  }

  ngOnInit(): void {
    this.filteredOmEntries = this.omEntries.slice();
    // this.omSelectionModel.changed.subscribe(value => {
    //   this.selectedOmEntries = this.omSelectionModel.selected.slice();
    // });
  }

  isAllSelected() {
    return this.omEntries && this.omSelectionModel.selected.length === this.omEntries.length;
  }

  masterToggle() {
    this.isAllSelected() ? this.omSelectionModel.clear() : this.omEntries.forEach(row => this.omSelectionModel.select(row));
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

  changeDate(emittedDate: string): void {
    this.selectedDate = emittedDate;
  }

  filterOmEntriesByEmployee(filterString: string): void {
    if (!filterString) {
      this.filteredOmEntries = this.omEntries.slice();
      return;
    }
    filterString = filterString.toLowerCase();
    this.filteredOmEntries = this.omEntries.filter(omEntry => {
      return omEntry.employee.firstName.toLowerCase().includes(filterString) ||
        omEntry.employee.sureName.toLowerCase().includes(filterString);
    });
  }

  releaseEmployees() {
    // if (this.selectedDate) {
    //   this.updateEmployeesSubscription = this.displayEmployeeListService.updateEmployees(this.selectedEmployees, this.selectedDate)
    //     .subscribe((res) => {
    //       // refresh employees
    //       this.employees = null;
    //       this.getAllEmployees();
    //       this.notificationService.showSuccess('Mitarbeiter erfolgreich aktualisiert!');
    //     });
    // }
  }
}
