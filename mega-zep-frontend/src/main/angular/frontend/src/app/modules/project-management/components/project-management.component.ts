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
import {Comment} from "../../shared/models/Comment";
import {Employee} from "../../shared/models/Employee";
import {Step} from "../../shared/models/Step";

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
  pmSelectionModels: Array<SelectionModel<ManagementEntry>>;
  environment = environment;

  constructor(private dialog: MatDialog,
              private pmService: ProjectManagementService,
              private stepEntryService: StepentriesService,
              private commentService: CommentService) {
  }

  ngOnInit(): void {
    this.getPmEntries();
  }

  areAllSelected(projectIndex: number, projectName: string) {
    return this.pmSelectionModels[projectIndex].selected.length === this.findEntriesForProject(projectName).length;
  }

  masterToggle(projectIndex: number, projectName: string) {
    this.areAllSelected(projectIndex, projectName) ?
      this.pmSelectionModels[projectIndex].clear() :
      this.findEntriesForProject(projectName).forEach(row => this.pmSelectionModels[projectIndex].select(row));
  }

  openDialog(employee: Employee, project: string): void {
    this.commentService.getCommentsForEmployee(employee).subscribe((comments: Array<Comment>) => {
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
      dialogRef.afterClosed().subscribe(() => this.getPmEntries());
    });
  }

  isAnySelected(): boolean {
    if (this.pmSelectionModels) {
      return this.pmSelectionModels.filter(pmSelectionModel => pmSelectionModel.selected.length > 0).length > 0;
    }
  }

  areAllProjectCheckStatesDone(projectName: string): boolean {
    return this.findEntriesForProject(projectName).every(entry => entry.projectCheckState === State.DONE);
  }

  closeProjectCheckForSelected() {
    this.pmSelectionModels
      .filter(pmSelectionModel => pmSelectionModel.selected.length > 0)
      .forEach(selectionModel => selectionModel.selected.forEach(entry => {
        console.log(entry.employee.email);
        // TODO call stepEntryService.closeProjectCheck
      }));
  }

  closeProjectCheck(projectName: string, row: ManagementEntry) {
    this.stepEntryService.closeProjectCheck(row.employee, projectName).subscribe(() => {
    });
    row.projectCheckState = State.DONE;
  }

  private getPmEntries() {
    this.pmService.getEntries().subscribe((pmEntries: Array<ProjectManagementEntry>) => {
      this.pmEntries = pmEntries;
      this.pmSelectionModels = [];
      this.pmEntries.forEach(
        () => this.pmSelectionModels.push(new SelectionModel<ManagementEntry>(true, []))
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
      })

      if (entries.length > 0) {
        return new Date(entries[0][0].entryDate);
      }
    }

    return new Date();
  }
}
