import {Component, OnInit} from '@angular/core';
import {ProjectManagementEntry} from '../models/ProjectManagementEntry';
import {MatDialog} from '@angular/material/dialog';
import {CommentsForEmployeeComponent} from '../../shared/components/comments-for-employee/comments-for-employee.component';
import {SelectionModel} from '@angular/cdk/collections';
import {State} from '../../shared/models/State';
import {ProjectManagementService} from '../services/project-management.service';
import {ManagementEntry} from '../../shared/models/ManagementEntry';
import {StepentriesService} from '../../shared/services/stepentries/stepentries.service';
import {environment} from '../../../../environments/environment';
import {CommentService} from '../../shared/services/comment/comment.service';
import {Comment} from '../../shared/models/Comment';
import {Employee} from '../../shared/models/Employee';
import {Step} from '../../shared/models/Step';

import * as _moment from 'moment';
import {Moment} from 'moment';
import {ConfigService} from '../../shared/services/config/config.service';
import {Config} from '../../shared/models/Config';
import {configuration} from '../../shared/constants/configuration';
import {ProjectState} from '../../shared/models/ProjectState';
import {MatSelectChange} from '@angular/material/select';
import {ProjectEntriesService} from '../../shared/services/projectentries/project-entries.service';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {TranslateService} from '@ngx-translate/core';
import {ProjectStateSelectComponent} from '../../shared/components/project-state-select/project-state-select.component';
import {ProjectCommentService} from '../../shared/services/project-comment/project-comment.service';
import {SnackbarService} from '../../shared/services/snackbar/snackbar.service';

const moment = _moment;

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {

  pmEntries: Array<ProjectManagementEntry>;
  displayedColumns = [
    'select',
    'employeeName',
    'projectCheckState',
    'employeeCheckState',
    'internalCheckState',
    'customerCheckState',
    'doneCommentsIndicator',
    'projectHours'
  ];

  officeManagementUrl: string;
  pmSelectionModels: Map<string, SelectionModel<ManagementEntry>>;
  environment = environment;
  selectedYear = moment().subtract(1, 'month').year();
  selectedMonth = moment().subtract(1, 'month').month() + 1; // months 0 - 11
  showCommentEditor = false;
  forProjectName: string;
  tooltipShowDelay = 500;
  tooltipPosition = 'above';
  maxMonthDate: number = 1;

  constructor(private dialog: MatDialog,
              private pmService: ProjectManagementService,
              private stepEntryService: StepentriesService,
              private commentService: CommentService,
              private configService: ConfigService,
              private projectEntryService: ProjectEntriesService,
              private snackbarService: SnackbarService,
              private translate: TranslateService,
              private projectCommentService: ProjectCommentService) {
  }

  get date() {
    return moment()
      .year(this.selectedYear)
      .month(this.selectedMonth)
      .date(1)
      .startOf('day');
  }

  ngOnInit(): void {
    this.configService.getConfig().subscribe((config: Config) => {
      this.officeManagementUrl = config.zepOrigin + '/' + configuration.OFFICE_MANAGEMENT_SEGMENT;
    });
    this.getPmEntries();
  }

  dateChanged(date: Moment): void {
    this.selectedYear = moment(date).year();
    this.selectedMonth = moment(date).month() + 1;
    this.getPmEntries();
  }

  areAllSelected(projectName: string): boolean {
    return this.pmSelectionModels.get(projectName).selected.length === this.findEntriesForProject(projectName).length;
  }

  masterToggle(projectName: string): void {
    this.areAllSelected(projectName) ?
      this.pmSelectionModels.get(projectName).clear() :
      this.findEntriesForProject(projectName).forEach(row => this.pmSelectionModels.get(projectName).select(row));
  }

  openDialog(employee: Employee, project: string): void {
    this.commentService.getCommentsForEmployee(employee.email, this.getFormattedDate())
      .subscribe((comments: Array<Comment>) => {
        const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
          {
            width: '100%',
            autoFocus: false
          }
        );

        dialogRef.componentInstance.employee = employee;
        dialogRef.componentInstance.comments = comments;
        dialogRef.componentInstance.step = Step.CONTROL_TIME_EVIDENCES;
        dialogRef.componentInstance.project = project;
        dialogRef.componentInstance.currentMonthYear = this.getFormattedDate();

        dialogRef.disableClose = true;
        dialogRef.componentInstance.commentHasChanged.subscribe(() => this.getPmEntries())
      });
  }

  isAnySelected(): boolean {
    if (this.pmSelectionModels) {
      return Array.from(this.pmSelectionModels.values())
        .filter(pmSelectionModel => pmSelectionModel?.selected.length > 0).length > 0;
    }
  }

  areAllProjectCheckStatesDone(projectName: string): boolean {
    return this.findEntriesForProject(projectName).every(entry => entry.projectCheckState === State.DONE);
  }

  closeProjectCheckForSelected(): void {
    for (const [projectName, selectionModel] of this.pmSelectionModels.entries()) {
      if (selectionModel.selected.length > 0) {
        for (const entry of selectionModel.selected) {
          this.stepEntryService
            .closeProjectCheck(entry.employee, projectName, this.getFormattedDate())
            .subscribe(() => entry.projectCheckState = State.DONE);
        }
      }
    }
  }

  closeProjectCheck(projectName: string, row: ManagementEntry) {
    this.stepEntryService
      .closeProjectCheck(row.employee, projectName, this.getFormattedDate())
      .subscribe(() => row.projectCheckState = State.DONE);
  }

  private getPmEntries() {
    this.pmService.getEntries(this.selectedYear, this.selectedMonth, false).subscribe((pmEntries: Array<ProjectManagementEntry>) => {
      this.pmEntries = pmEntries;
      this.pmSelectionModels = new Map<string, SelectionModel<ManagementEntry>>();
      this.pmEntries.sort((a, b) => a.projectName.localeCompare(b.projectName));
      this.pmEntries.forEach(pmEntry => {
          this.pmSelectionModels.set(pmEntry.projectName, new SelectionModel<ManagementEntry>(true, []));

          const allDone = this.getFilteredAndSortedPmEntries(pmEntry, State.DONE, State.DONE, State.DONE, State.DONE);
          const notAllDone = pmEntry.entries.filter(entry => !allDone.find(done => done.employee.email === entry.employee.email))
            .sort((a, b) => a.employee.lastname.concat(a.employee.firstname)
              .localeCompare(b.employee.lastname.concat(b.employee.firstname)));

          pmEntry.entries = notAllDone.concat(allDone);

          this.projectCommentService.get(this.getFormattedDate(), pmEntry.projectName)
            .subscribe(projectComment => {
              pmEntry.projectComment = projectComment;
            });
        }
      );
    });
  }

  getFilteredAndSortedPmEntries(pmEntry: ProjectManagementEntry, customerCheckState: State, projectCheckState: State, employeeCheckState: State, internalCheckState: State): Array<ManagementEntry> {
    return pmEntry.entries
      .filter(val => val.customerCheckState === customerCheckState && val.projectCheckState === projectCheckState &&
        val.employeeCheckState === employeeCheckState && val.internalCheckState === internalCheckState)
      .sort((a, b) => a.employee.lastname.concat(a.employee.firstname)
        .localeCompare(b.employee.lastname.concat(b.employee.firstname)));
  }

  private findEntriesForProject(projectName: string) {
    return this.pmEntries.filter(entry => {
      return entry.projectName === projectName;
    })[0].entries;
  }

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format('yyyy-MM-DD');
  }

  onChangeControlProjectState($event: MatSelectChange, pmEntry: ProjectManagementEntry, controlProjectStateSelect: ProjectStateSelectComponent): void {
    const newValue = $event.value as ProjectState;
    const preset = newValue !== 'NOT_RELEVANT' ? false : pmEntry.presetControlProjectState;

    this.projectEntryService.updateProjectEntry(newValue, preset, pmEntry.projectName, 'CONTROL_PROJECT', this.getFormattedDate())
      .subscribe((success) => {
        if (success) {
          pmEntry.controlProjectState = newValue;
          pmEntry.presetControlProjectState = preset;
        } else {
          this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.updateStatusError'));
          controlProjectStateSelect.value = pmEntry.controlProjectState;
        }
      });
  }

  onChangeControlBillingState($event: MatSelectChange, pmEntry: ProjectManagementEntry, controlBillingStateSelect: ProjectStateSelectComponent): void {
    const newValue = $event.value as ProjectState;
    const preset = newValue !== 'NOT_RELEVANT' ? false : pmEntry.presetControlBillingState;

    this.projectEntryService.updateProjectEntry(newValue, preset, pmEntry.projectName, 'CONTROL_BILLING', this.getFormattedDate())
      .subscribe((success) => {
        if (success) {
          pmEntry.controlBillingState = newValue;
          pmEntry.presetControlBillingState = preset;
        } else {
          this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.updateStatusError'));
          controlBillingStateSelect.value = pmEntry.controlBillingState;
        }
      });
  }

  onChangePresetControlProjectState($event: MatCheckboxChange, pmEntry: ProjectManagementEntry): void {
    this.projectEntryService.updateProjectEntry(pmEntry.controlProjectState, pmEntry.presetControlProjectState, pmEntry.projectName, 'CONTROL_PROJECT', this.getFormattedDate())
      .subscribe(success => {
        if (!success) {
          this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.updateStatusError'));
          pmEntry.presetControlProjectState = !$event.checked;
        }
      });
  }

  onChangePresetControlBillingState($event: MatCheckboxChange, pmEntry: ProjectManagementEntry): void {
    this.projectEntryService.updateProjectEntry(pmEntry.controlBillingState, pmEntry.presetControlBillingState, pmEntry.projectName, 'CONTROL_BILLING', this.getFormattedDate())
      .subscribe(success => {
        if (!success) {
          this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.updateStatusError'));
          pmEntry.presetControlBillingState = !$event.checked;
        }
      })
  }

  isProjectStateNotRelevant(projectState: ProjectState): boolean {
    return projectState === ProjectState.NOT_RELEVANT;
  }

  onStartEditing(projectName: string): void {
    this.forProjectName = projectName;
    this.showCommentEditor = true;
  }

  onCommentChange(pmEntry: ProjectManagementEntry, comment: string): void {
    this.showCommentEditor = false;
    this.forProjectName = null;

    let returnClicked = false;

    // Avoid reloading of page when the return button was clicked
    if (pmEntry.projectComment) {
      if (pmEntry.projectComment.comment !== comment) {
        let oldComment = pmEntry.projectComment.comment;
        pmEntry.projectComment.comment = comment;
        this.projectCommentService.update(pmEntry.projectComment)
          .subscribe((success) => {
            if (!success) {
              this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.updateProjectCommentError'));
              pmEntry.projectComment.comment = oldComment;
            }
          });
      } else {
        returnClicked = true;
      }
    } else {
      // Avoid reloading of page when the return button was clicked
      if (comment) {
        this.projectCommentService.create(comment, this.getFormattedDate(), pmEntry.projectName)
          .subscribe(projectComment => {
            pmEntry.projectComment = projectComment;
          });
      } else {
        returnClicked = true;
      }
    }
    if (returnClicked) {
      this.snackbarService.showSnackbarWithMessage(this.translate.instant('project-management.projectCommentNotUpdated'));
    }
  }

  convertDurationToTime(durationInSeconds: number): number {
    return durationInSeconds / 60 / 60;
  }
}
