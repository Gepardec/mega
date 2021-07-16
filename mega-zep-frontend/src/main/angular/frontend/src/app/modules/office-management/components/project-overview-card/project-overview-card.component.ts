import {Component, OnDestroy, OnInit} from '@angular/core';
import * as _moment from 'moment';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {configuration} from '../../../shared/constants/configuration';
import {environment} from '../../../../../environments/environment';
import {MatDialog} from '@angular/material/dialog';
import {OfficeManagementService} from '../../services/office-management.service';
import {NotificationService} from '../../../shared/services/notification/notification.service';
import {TranslateService} from '@ngx-translate/core';
import {CommentService} from '../../../shared/services/comment/comment.service';
import {StepentriesService} from '../../../shared/services/stepentries/stepentries.service';
import {ConfigService} from '../../../shared/services/config/config.service';
import {Config} from '../../../shared/models/Config';
import {State} from '../../../shared/models/State';
import {ProjectManagementEntry} from '../../../project-management/models/ProjectManagementEntry';
import {ProjectManagementService} from '../../../project-management/services/project-management.service';
import {Subscription, zip} from 'rxjs';
import {tap} from 'rxjs/operators';
import {ProjectState} from '../../../shared/models/ProjectState';
import {ProjectCommentService} from '../../../shared/services/project-comment/project-comment.service';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from '@angular/material/snack-bar';

const moment = _moment;


@Component({
  selector: 'app-project-overview-card',
  templateUrl: './project-overview-card.component.html',
  styleUrls: ['./project-overview-card.component.scss']
})
export class ProjectOverviewCardComponent implements OnInit, OnDestroy {
  State = State;

  employeeProgressRef: MatBottomSheetRef;

  displayedColumns = [
    'name',
    'controlEmployeesState',
    'controlProjectState',
    'controlBillingState',
    'comment'
  ];

  officeManagementUrl: string;
  pmEntries: Array<ProjectManagementEntry>;
  // pmSelectionModel = new SelectionModel<ManagementEntry>(true, []);
  selectedDate: string;
  dayOfMonthForWarning = 5;
  configuration = configuration;
  environment = environment;
  selectedYear: number;
  selectedMonth: number;
  dateSelectionSub: Subscription;
  showCommentEditor = false;
  forProjectName: string;

  constructor(
    private dialog: MatDialog,
    private omService: OfficeManagementService,
    private pmService: ProjectManagementService,
    private notificationService: NotificationService,
    private translateService: TranslateService,
    private commentService: CommentService,
    private stepEntryService: StepentriesService,
    private _bottomSheet: MatBottomSheet,
    private configService: ConfigService,
    private projectCommentService: ProjectCommentService,
    private _snackBar: MatSnackBar,
    private translate: TranslateService) {
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
        this.getPmEntries();
      });
  }

  ngOnDestroy(): void {
    if (this.dateSelectionSub) {
      this.dateSelectionSub.unsubscribe();
    }
  }

  private getPmEntries() {
    this.pmService.getEntries(this.selectedYear, this.selectedMonth).subscribe((pmEntries: Array<ProjectManagementEntry>) => {
      this.pmEntries = pmEntries;
      this.pmEntries.forEach(pmEntry => {
        this.projectCommentService.getProjectComment(this.getFormattedDate(), pmEntry.projectName)
          .subscribe(projectComment => {
            pmEntry.projectComment = projectComment;
          });
      });
    });
  }

  isAtLeastOneEmployeeCheckDone(pmEntry: ProjectManagementEntry): ProjectState {
    for (let mgmtEntry of pmEntry.entries) {
      if (mgmtEntry.projectCheckState === State.DONE) {
        return ProjectState.DONE;
      }
    }
    return ProjectState.OPEN;
  }

  onStartEditing(projectName: string) {
    this.forProjectName = projectName;
    this.showCommentEditor = true;
  }

  onCommentChange(pmEntry: ProjectManagementEntry, comment: string) {
    this.showCommentEditor = false;
    this.forProjectName = null;

    // Avoid reloading of page when the return button was clicked
    if (pmEntry.projectComment) {
      if (pmEntry.projectComment.comment !== comment) {
        let oldComment = pmEntry.projectComment.comment;
        pmEntry.projectComment.comment = comment;
        this.projectCommentService.updateProjectComment(pmEntry.projectComment)
          .subscribe((success) => {
            if (!success) {
              this.showErrorSnackbar();
              pmEntry.projectComment.comment = oldComment;
            }
          });
      }
    } else {
      // Avoid reloading of page when the return button was clicked
      if (comment) {
        this.projectCommentService.createNewProjectComment(comment, this.getFormattedDate(), pmEntry.projectName)
          .subscribe(projectComment => {
            pmEntry.projectComment = projectComment;
          });
      }
    }
  }

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format('yyyy-MM-DD');
  }

  // TODO: Refactor snackbar logic into service
  private showErrorSnackbar() {
    this._snackBar.open(
      this.translate.instant('snackbar.message'),
      this.translate.instant('snackbar.confirm'),
      {
        horizontalPosition: <MatSnackBarHorizontalPosition>configuration.snackbar.horizontalPosition,
        verticalPosition: <MatSnackBarVerticalPosition>configuration.snackbar.verticalPosition,
        duration: configuration.snackbar.duration
      });
  }
}
