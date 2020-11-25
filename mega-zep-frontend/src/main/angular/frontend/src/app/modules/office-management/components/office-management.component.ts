import {Component, OnInit} from '@angular/core';
import {ManagementEntry} from '../../shared/models/ManagementEntry';
import {State} from '../../shared/models/State';
import {MatDialog} from '@angular/material/dialog';
import {SelectionModel} from '@angular/cdk/collections';
import {configuration} from '../../shared/constants/configuration';
import {environment} from '../../../../environments/environment';
import {OfficeManagementService} from '../services/office-management.service';
import {NotificationService} from '../../shared/services/notification/notification.service';
import {TranslateService} from '@ngx-translate/core';
import {CommentService} from '../../shared/services/comment/comment.service';
import {Comment} from '../../shared/models/Comment';
import {CommentsForEmployeeComponent} from '../../shared/components/comments-for-employee/comments-for-employee.component';
import {StepentriesService} from '../../shared/services/stepentries/stepentries.service';
import {Step} from '../../shared/models/Step';

@Component({
  selector: 'app-office-management',
  templateUrl: './office-management.component.html',
  styleUrls: ['./office-management.component.scss']
})
export class OfficeManagementComponent implements OnInit {
  State = State;

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

  omEntries: Array<ManagementEntry>;
  filteredOmEntries: Array<ManagementEntry>;
  omSelectionModel = new SelectionModel<ManagementEntry>(true, []);
  selectedDate: string;
  dayOfMonthForWarning = 5;
  configuration = configuration;
  environment = environment;

  constructor(
    private dialog: MatDialog,
    private omService: OfficeManagementService,
    private notificationService: NotificationService,
    private translateService: TranslateService,
    private commentService: CommentService,
    private stepEntryService: StepentriesService) {
  }

  ngOnInit(): void {
    this.getOmEntries();
  }

  areAllSelected() {
    return this.omEntries && this.omSelectionModel.selected.length === this.omEntries.length;
  }

  masterToggle() {
    this.areAllSelected() ? this.omSelectionModel.clear() : this.omEntries.forEach(row => this.omSelectionModel.select(row));
  }

  openDialog(omEntry: ManagementEntry): void {
    this.commentService.getCommentsForEmployee(omEntry.employee).subscribe((comments: Array<Comment>) => {
      const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
        {
          width: '100%',
          autoFocus: false
        }
      );

      dialogRef.componentInstance.employee = omEntry.employee;
      dialogRef.componentInstance.comments = comments;
      dialogRef.afterClosed().subscribe(() => this.getOmEntries());
    });
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

  filterOmEntriesByOMC1Status(checked: boolean) {
    if (checked === true) {
      this.filteredOmEntries = this.omEntries.filter(val => val.customerCheckState === State.OPEN);
    } else {
      this.filteredOmEntries = this.omEntries.slice();
    }
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
      this.getOmEntries();
      const successMessage = await this.translateService.get('notifications.employeesUpdatedSuccess').toPromise();
      this.notificationService.showSuccess(successMessage);
    });
  }

  closeCustomerCheck(omEntry: ManagementEntry) {
    this.stepEntryService.close(omEntry.employee, Step.CONTROL_EXTERNAL_TIMES).subscribe(() => {
      omEntry.customerCheckState = State.DONE;
    });
  }

  closeInternalCheck(omEntry: ManagementEntry) {
    this.stepEntryService.close(omEntry.employee, Step.CONTROL_INTERNAL_TIMES).subscribe(() => {
      omEntry.internalCheckState = State.DONE;
    });
  }

  private getOmEntries() {
    this.omService.getEntries().subscribe((omEntries: Array<ManagementEntry>) => {
      this.omEntries = omEntries;
      this.sortOmEntries();
    });
  }

  private sortOmEntries(): void {
    const sortFn = (a: ManagementEntry, b: ManagementEntry) => a.employee.sureName.localeCompare(b.employee.sureName);
    this.omEntries = this.omEntries.sort(sortFn);
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
