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
import {ProjectEntriesService} from "../../shared/services/projectentries/project-entries.service";
import {MatCheckboxChange} from "@angular/material/checkbox";

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
    'releaseDate'
  ];

  officeManagementUrl: string;
  pmSelectionModels: Map<string, SelectionModel<ManagementEntry>>;
  environment = environment;
  selectedYear = moment().subtract(1, 'month').year();
  selectedMonth = moment().subtract(1, 'month').month() + 1; // months 0 - 11


  constructor(private dialog: MatDialog,
              private pmService: ProjectManagementService,
              private stepEntryService: StepentriesService,
              private commentService: CommentService,
              private configService: ConfigService,
              private projectEntryService: ProjectEntriesService) {
  }

  ngOnInit(): void {
    this.configService.getConfig().subscribe((config: Config) => {
      this.officeManagementUrl = config.zepOrigin + '/' + configuration.OFFICE_MANAGEMENT_SEGMENT;
    });
    this.getPmEntries();
  }

  dateChanged(date: Moment) {
    this.selectedYear = moment(date).year();
    this.selectedMonth = moment(date).month() + 1;
    this.getPmEntries();
  }

  areAllSelected(projectName: string) {
    return this.pmSelectionModels.get(projectName).selected.length === this.findEntriesForProject(projectName).length;
  }

  masterToggle(projectName: string) {
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

  closeProjectCheckForSelected() {
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
    this.pmService.getEntries(this.selectedYear, this.selectedMonth).subscribe((pmEntries: Array<ProjectManagementEntry>) => {
      this.pmEntries = pmEntries;
      this.pmSelectionModels = new Map<string, SelectionModel<ManagementEntry>>();
      this.pmEntries.forEach(pmEntry =>
        this.pmSelectionModels.set(pmEntry.projectName, new SelectionModel<ManagementEntry>(true, []))
      );
    });
  }

  private findEntriesForProject(projectName: string) {
    return this.pmEntries.filter(entry => {
      return entry.projectName === projectName;
    })[0].entries;
  }

  getCurrentReleaseDate(): Date {
    if (this.pmEntries) {
      const entries = [];

      this.pmEntries.forEach(pmEntry => {
        entries.push(pmEntry.entries.filter(entry => {
          return entry.projectCheckState === State.OPEN ||
            entry.customerCheckState === State.OPEN ||
            entry.employeeCheckState === State.OPEN ||
            entry.internalCheckState === State.OPEN;
        }));
      });

      if (entries.length > 0) {
        return new Date(entries[0][0].entryDate);
      }
    }

    return new Date();
  }

  private getFormattedDate() {
    return moment().year(this.selectedYear).month(this.selectedMonth - 1).date(1).format('yyyy-MM-DD');
  }

  onChangeProjectControllingState($event: MatSelectChange, pmEntry: ProjectManagementEntry) {
    const newValue = $event.value as ProjectState;
    const preset = newValue !== 'NOT_RELEVANT' ? false : pmEntry.presetControlProjectState;

    this.projectEntryService.updateProjectEntry(newValue, preset, pmEntry.projectName, 'CONTROL_PROJECT', this.getFormattedDate())
      .subscribe((success) => {
        if (success) {
          pmEntry.controlProjectState = newValue;
        } else {
          // TODO error handling
        }
      });
  }

  onChangeProjectBillingState($event: MatSelectChange, pmEntry: ProjectManagementEntry) {
    const newValue = $event.value as ProjectState;
    const preset = newValue !== 'NOT_RELEVANT' ? false : pmEntry.presetControlBillingState;

    this.projectEntryService.updateProjectEntry(newValue, preset, pmEntry.projectName, 'CONTROL_BILLING', this.getFormattedDate())
      .subscribe((success) => {
        if (success) {
          pmEntry.controlBillingState = newValue;
        } else {
          // TODO error handling
        }
      });
  }

  onChangeProjectControllingPreset($event: MatCheckboxChange, pmEntry: ProjectManagementEntry) {
    this.projectEntryService.updateProjectEntry(pmEntry.controlProjectState, pmEntry.presetControlProjectState, pmEntry.projectName, 'CONTROL_PROJECT', this.getFormattedDate())
      .subscribe(success => {
        if (!success) {
          // TODO error handling
        }
      })
  }

  onChangeControlBillingPreset($event: MatCheckboxChange, pmEntry: ProjectManagementEntry) {
    this.projectEntryService.updateProjectEntry(pmEntry.controlBillingState, pmEntry.presetControlBillingState, pmEntry.projectName, 'CONTROL_BILLING', this.getFormattedDate())
      .subscribe(success => {
        if (!success) {
          // TODO error handling
        }
      })
  }

  isProjectStateNotRelevant(projectState: ProjectState) {
    return projectState === ProjectState.NOT_RELEVANT;
  }
}
