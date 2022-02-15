import {Component, OnDestroy, OnInit} from '@angular/core';
import {ManagementEntry} from '../../../shared/models/ManagementEntry';
import {State} from '../../../shared/models/State';
import {MatDialog} from '@angular/material/dialog';
import {SelectionModel} from '@angular/cdk/collections';
import {configuration} from '../../../shared/constants/configuration';
import {environment} from '../../../../../environments/environment';
import {OfficeManagementService} from '../../services/office-management.service';
import {NotificationService} from '../../../shared/services/notification/notification.service';
import {TranslateService} from '@ngx-translate/core';
import {CommentService} from '../../../shared/services/comment/comment.service';
import {Comment} from '../../../shared/models/Comment';
import {CommentsForEmployeeComponent} from '../../../shared/components/comments-for-employee/comments-for-employee.component';
import {StepentriesService} from '../../../shared/services/stepentries/stepentries.service';
import {Step} from '../../../shared/models/Step';

import * as _moment from 'moment';
import {Moment} from 'moment';
import {PmProgressComponent} from '../../../shared/components/pm-progress/pm-progress.component';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {ConfigService} from '../../../shared/services/config/config.service';
import {Config} from '../../../shared/models/Config';
import {Subscription, zip} from 'rxjs';
import {tap} from 'rxjs/operators';

const moment = _moment;

@Component({
  selector: 'app-employee-card',
  templateUrl: './employee-card.component.html',
  styleUrls: ['./employee-card.component.scss']
})
export class EmployeeCardComponent implements OnInit, OnDestroy {
  State = State;

  employeeProgressRef: MatBottomSheetRef;

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

  officeManagementUrl: string;
  omEntries: Array<ManagementEntry>;
  filteredOmEntries: Array<ManagementEntry>;
  omSelectionModel = new SelectionModel<ManagementEntry>(true, []);
  selectedDate: string;
  dayOfMonthForWarning = 5;
  configuration = configuration;
  environment = environment;
  selectedYear: number;
  selectedMonth: number;
  dateSelectionSub: Subscription;

  constructor(
    private dialog: MatDialog,
    private omService: OfficeManagementService,
    private notificationService: NotificationService,
    private translateService: TranslateService,
    private commentService: CommentService,
    private stepEntryService: StepentriesService,
    private _bottomSheet: MatBottomSheet,
    private configService: ConfigService) {
  }

  ngOnInit(): void {
    this.configService.getConfig().subscribe((config: Config) => {
      this.officeManagementUrl = config.zepOrigin + '/' + configuration.OFFICE_MANAGEMENT_SEGMENT;
    });

    this.dateSelectionSub = zip(this.omService.selectedYear, this.omService.selectedMonth)
      .pipe(
        tap(value => {
          this.selectedYear = value[0];
          this.selectedMonth = value[1];
        })
      ).subscribe(() => {
        this.getOmEntries();
      });
  }

  ngOnDestroy(): void {
    if (this.dateSelectionSub) {
      this.dateSelectionSub.unsubscribe();
    }
  }

  dateChanged(date: Moment) {
    this.selectedYear = moment(date).year();
    this.selectedMonth = moment(date).month() + 1;
    this.getOmEntries();
  }

  areAllSelected() {
    return this.omEntries && this.omSelectionModel.selected.length === this.omEntries.length;
  }

  masterToggle() {
    this.areAllSelected() ? this.omSelectionModel.clear() : this.omEntries.forEach(row => this.omSelectionModel.select(row));
  }

  openDialog(omEntry: ManagementEntry): void {
    this.commentService
      .getCommentsForEmployee(omEntry.employee.email, this.getFormattedDate())
      .subscribe((comments: Array<Comment>) => {
        const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
          {
            width: '100%',
            autoFocus: false
          }
        );

        dialogRef.componentInstance.employee = omEntry.employee;
        dialogRef.componentInstance.comments = comments;
        dialogRef.componentInstance.step = Step.CONTROL_INTERNAL_TIMES;
        dialogRef.componentInstance.currentMonthYear = this.getFormattedDate();

        dialogRef.disableClose = true;
        dialogRef.componentInstance.commentHasChanged.subscribe(() => this.getOmEntries());
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
      return omEntry.employee.firstname.toLowerCase().includes(filterString) ||
        omEntry.employee.lastname.toLowerCase().includes(filterString);
    });
  }

  getFilteredAndSortedOmEntries(customerCheckState: State, internalCheckState: State) {
    return this.omEntries
      .filter(val => val.customerCheckState === customerCheckState && val.internalCheckState === internalCheckState)
      .sort((a, b) => a.employee.lastname.concat(a.employee.firstname)
        .localeCompare(b.employee.lastname.concat(b.employee.firstname)));
  }

  getCurrentReleaseDate(): Date {
    const entries = this.omEntries.filter(entry => {
      return entry.projectCheckState === State.OPEN ||
        entry.customerCheckState === State.OPEN ||
        entry.employeeCheckState === State.OPEN ||
        entry.internalCheckState === State.OPEN;
    });

    if (entries.length > 0) {
      return new Date(entries[0].entryDate);
    }

    return new Date();
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

    this.omService.updateEmployees(employees).subscribe(async () => {
      this.filteredOmEntries = null;
      this.getOmEntries();
      const successMessage = await this.translateService.get('notifications.employeesUpdatedSuccess').toPromise();
      this.notificationService.showSuccess(successMessage);
    });
  }

  closeCustomerCheck(omEntry: ManagementEntry) {
    this.stepEntryService
      .closeOfficeCheck(omEntry.employee, Step.CONTROL_EXTERNAL_TIMES, this.getFormattedDate())
      .subscribe(() => {
        omEntry.customerCheckState = State.DONE;
      });
  }

  closeInternalCheck(omEntry: ManagementEntry) {
    this.stepEntryService
      .closeOfficeCheck(omEntry.employee, Step.CONTROL_INTERNAL_TIMES, this.getFormattedDate())
      .subscribe(() => {
        omEntry.internalCheckState = State.DONE;
      });
  }

  openEmployeeProgress(omEntry: ManagementEntry) {
    this.employeeProgressRef = this._bottomSheet.open(PmProgressComponent, {
      data: {employeeProgresses: omEntry.employeeProgresses},
      autoFocus: false,
      hasBackdrop: false
    });
  }

  closeEmployeeProgress() {
    this.employeeProgressRef.dismiss();
  }

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format(configuration.dateFormat);
  }

  private getOmEntries() {
    this.omService.getEntries(this.selectedYear, this.selectedMonth).subscribe((omEntries: Array<ManagementEntry>) => {
      this.omEntries = omEntries;
      this.sortOmEntries();
    });
  }

  private sortOmEntries(): void {
    const allDoneEntries = this.getFilteredAndSortedOmEntries(State.DONE, State.DONE);
    const projectEntriesDone = this.getFilteredAndSortedOmEntries(State.DONE, State.OPEN);
    const internalEntriesDone = this.getFilteredAndSortedOmEntries(State.OPEN, State.DONE);
    const allOpenEntries = this.getFilteredAndSortedOmEntries(State.OPEN, State.OPEN);

    this.filteredOmEntries = allOpenEntries
      .concat(projectEntriesDone)
      .concat(internalEntriesDone)
      .concat(allDoneEntries);
  }

  private monthDiff(d1: Date, d2: Date) {
    let months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return Math.abs(months);
  }
}
