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
import {MatOptionSelectionChange} from '@angular/material/core';

interface Month {
  value: number;
  viewValue: string;
}

interface Year {
  value: number;
  viewValue: string;
}

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


  months: Month[] = [
    {value: 1, viewValue: 'January'},
    {value: 2, viewValue: 'February'},
    {value: 3, viewValue: 'March'},
    {value: 4, viewValue: 'April'},
    {value: 5, viewValue: 'May'},
    {value: 6, viewValue: 'June'},
    {value: 7, viewValue: 'July'},
    {value: 8, viewValue: 'August'},
    {value: 9, viewValue: 'September'},
    {value: 10, viewValue: 'October'},
    {value: 11, viewValue: 'November'},
    {value: 12, viewValue: 'December'}
  ];

  years: Year[] = [
    {value: 2018, viewValue: '2018'},
    {value: 2019, viewValue: '2019'},
    {value: 2020, viewValue: '2020'}
  ];

  pmSelectionModels: Map<string, SelectionModel<ManagementEntry>>;
  environment = environment;
  selectedYear = 2020;
  selectedMonth = 11;

  constructor(private dialog: MatDialog,
              private pmService: ProjectManagementService,
              private stepEntryService: StepentriesService,
              private commentService: CommentService) {
  }

  ngOnInit(): void {
    this.getPmEntries();
  }

  yearChanged(event: MatOptionSelectionChange): void {
    if (event.isUserInput === true) {
      this.selectedYear = event.source.value;
      this.getPmEntries();
    }
  }

  monthChanged(event: MatOptionSelectionChange): void {
    if (event.isUserInput === true) {
      this.selectedMonth = event.source.value;
      this.getPmEntries();
    }
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
      dialogRef.afterClosed().subscribe(() => this.getPmEntries());
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
    return this.selectedYear + '-' + this.selectedMonth + '-01';
  }
}
