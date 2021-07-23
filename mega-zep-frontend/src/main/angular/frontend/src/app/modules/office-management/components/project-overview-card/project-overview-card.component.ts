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
import {SnackbarService} from '../../../shared/services/snackbar/snackbar.service';

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
    private translate: TranslateService,
    private commentService: CommentService,
    private stepEntryService: StepentriesService,
    private _bottomSheet: MatBottomSheet,
    private configService: ConfigService,
    private projectCommentService: ProjectCommentService,
    private snackbarService: SnackbarService) {
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
        this.projectCommentService.get(this.getFormattedDate(), pmEntry.projectName)
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

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format('yyyy-MM-DD');
  }
}
