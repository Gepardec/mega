import {Component, OnInit} from '@angular/core';
import {OfficeManagementEntry} from '../models/OfficeManagementEntry';
import {State} from '../../shared/models/State';
import {MatDialog} from '@angular/material/dialog';
import {SelectionModel} from '@angular/cdk/collections';
import {configuration} from '../../shared/constants/configuration';
import {environment} from '../../../../environments/environment';
import {OfficeManagementService} from '../services/office-management.service';
import {NotificationService} from '../../shared/services/notification/notification.service';
import {TranslateService} from '@ngx-translate/core';
import {CommentService} from '../../shared/services/comment/comment.service';

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
    this.getOmEntries();
  }

  areAllSelected() {
    return this.omEntries && this.omSelectionModel.selected.length === this.omEntries.length;
  }

  masterToggle() {
    this.areAllSelected() ? this.omSelectionModel.clear() : this.omEntries.forEach(row => this.omSelectionModel.select(row));
  }

  openDialog(omEntry: OfficeManagementEntry): void {
    // const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
    //   {
    //     width: '100%',
    //     autoFocus: false
    //   }
    // );
    //
    // dialogRef.componentInstance.employee = omEntry.employee.firstName + ' ' + omEntry.employee.sureName;
    // dialogRef.componentInstance.comments = omEntry.comments;
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
      this.getOmEntries();
      const successMessage = await this.translateService.get('notifications.employeesUpdatedSuccess').toPromise();
      this.notificationService.showSuccess(successMessage);
    });
  }

  private getOmEntries() {
    this.omService.getEntries().subscribe((omEntries: Array<OfficeManagementEntry>) => {
      this.omEntries = omEntries;
      this.sortOmEntries();
    });
  }

  private sortOmEntries(): void {
    const sortFn = (a: OfficeManagementEntry, b: OfficeManagementEntry) => a.employee.sureName.localeCompare(b.employee.sureName);
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
