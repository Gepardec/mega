import { Component, OnInit } from '@angular/core';
import { OfficeManagementEntry } from '../models/OfficeManagementEntry';
import { State } from '../../shared/models/State';
import { MatDialog } from '@angular/material/dialog';
import { CommentsForEmployeeComponent } from '../../shared/components/comments-for-employee/comments-for-employee.component';
import { SelectionModel } from '@angular/cdk/collections';
import { configuration } from '../../shared/constants/configuration';
import { environment } from '../../../../environments/environment';
import { OfficeManagementService } from '../services/office-management.service';
import { omEntriesMock } from '../models/MockData';
import { NotificationService } from '../../shared/services/notification/notification.service';
import { TranslateService } from '@ngx-translate/core';
import { CommentService } from '../../shared/services/comment/comment.service';

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
    'actions',
    'employeeCheckState',
    'projectCheckState',
    'releaseDate'
  ];
  State = State;
  omEntries: Array<OfficeManagementEntry>;
  filteredOmEntries: Array<OfficeManagementEntry>;
  omSelectionModel = new SelectionModel<OfficeManagementEntry>(true, []);
  selectedDate: string;
  dayOfMonthForWarning = 5;
  configuration = configuration;
  environment = environment;

  constructor(
    private dialog: MatDialog,
    private omService: OfficeManagementService,
    private notificationService: NotificationService,
    private translateService: TranslateService,
    private commentService: CommentService) {
  }

  ngOnInit(): void {
    this.fillOmEntries();
  }

  areAllSelected() {
    return this.omEntries && this.omSelectionModel.selected.length === this.omEntries.length;
  }

  masterToggle() {
    this.areAllSelected() ? this.omSelectionModel.clear() : this.omEntries.forEach(row => this.omSelectionModel.select(row));
  }

  openDialog(omEntry: OfficeManagementEntry): void {
    const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
      {
        width: '100%',
        autoFocus: false
      }
    );

    dialogRef.componentInstance.employee = omEntry.employee.firstName + ' ' + omEntry.employee.sureName;
    dialogRef.componentInstance.comments = omEntry.comments;
  }

  changeDate(emittedDate: string): void {
    this.selectedDate = emittedDate;
  }

  filterOmEntriesByEmployeeName(filterString: string): void {
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

  getReleaseDateCssClass(date: string): string {
    const today = new Date();
    const releaseDate = new Date(date);
    const monthDiff = this.monthDiff(releaseDate, today);
    if (monthDiff === 1 || monthDiff === 0 || releaseDate > today) {
      return 'done';
    }
    if (monthDiff === 2 && today.getDate() <= this.dayOfMonthForWarning) {
      return 'wip';
    }
    return 'open';
  }

  releaseEmployees() {
    const employees = this.omSelectionModel.selected.map(omEntry => {
      omEntry.employee.releaseDate = this.selectedDate;
      return omEntry.employee;
    });

    this.omService.updateEmployees(employees).subscribe(async (res) => {
      this.filteredOmEntries = null;
      this.fillOmEntries();
      const successMessage = await this.translateService.get('notifications.employeesUpdatedSuccess').toPromise();
      this.notificationService.showSuccess(successMessage);
    });
  }

  private fillOmEntries() {
    this.omService.getEmployees().subscribe(employees => {
      this.omEntries = omEntriesMock;
      for (let i = 0; i < this.omEntries.length; i++) {
        if (employees.length > i) {
          this.omEntries[i].employee = employees[i];
        }
      }
      this.sortOmEntries();
    });
  }

  private sortOmEntries(): void {
    const doneOmEntries = [];
    const openOmEntries = [];

    this.omEntries.forEach(omEntry => {
      const diff = this.commentService.getDoneCommentsCount(omEntry.comments) - omEntry.comments.length;
      if (diff === 0 && omEntry.customerCheckState === State.DONE && omEntry.internalCheckState === State.DONE) {
        doneOmEntries.push(omEntry);
      } else {
        openOmEntries.push(omEntry);
      }
    });

    const sortFn = (a: OfficeManagementEntry, b: OfficeManagementEntry) => a.employee.sureName.localeCompare(b.employee.sureName);

    doneOmEntries.sort(sortFn);
    openOmEntries.sort(sortFn);

    this.omEntries = [].concat(openOmEntries, doneOmEntries);

    this.filteredOmEntries = this.omEntries.slice();
  }

  // TODO: maybe switch to a library that does this kind of calculations
  private monthDiff(d1: Date, d2: Date) {
    let months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return Math.abs(months);
  }

}
