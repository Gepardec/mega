import {Component, OnDestroy, OnInit} from '@angular/core';
import * as _moment from 'moment';
import {Moment} from 'moment';
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
        this.getPmEntries();
      });
  }

  ngOnDestroy(): void {
    console.log('ProjectOverviewCardComponent destroyed');
    if (this.dateSelectionSub) {
      this.dateSelectionSub.unsubscribe();
    }
  }

  dateChanged(date: Moment) {
    this.selectedYear = moment(date).year();
    this.selectedMonth = moment(date).month() + 1;
    this.getPmEntries();
  }

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format('yyyy-MM-DD');
  }

  private getPmEntries() {
    this.pmService.getEntries(this.selectedYear, this.selectedMonth).subscribe((pmEntries: Array<ProjectManagementEntry>) => {
      this.pmEntries = pmEntries;
      // this.pmSelectionModel = new Map<string, SelectionModel<ManagementEntry>>();
      this.pmEntries.forEach(pmEntry => {
        pmEntry.comment = Math.random() > 0.5 ? 'Dummy comment' : null;
        // pmEntry.comment = 'Dummy text';
      });
    });
  }

  private monthDiff(d1: Date, d2: Date) {
    let months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return Math.abs(months);
  }

  // openEmployeeProgress(omEntry: ManagementEntry) {
  //   this.employeeProgressRef = this._bottomSheet.open(PmProgressComponent, {
  //     data: {employeeProgresses: omEntry.employeeProgresses},
  //     autoFocus: false,
  //     hasBackdrop: false
  //   });
  // }

  // closeEmployeeProgress() {
  //   this.employeeProgressRef.dismiss();
  // }
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
    console.log(comment);
    this.showCommentEditor = false;
    this.forProjectName = null;
    pmEntry.comment = comment;
  }
}
