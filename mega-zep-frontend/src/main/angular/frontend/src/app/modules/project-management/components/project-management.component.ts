import { Component, OnInit } from '@angular/core';
import { ProjectManagementEntry } from '../models/ProjectManagementEntry';
import projectManagementResponseMock from '../models/MockData';
import { MatDialog } from '@angular/material/dialog';
import { CommentsForEmployeeComponent } from '../../shared/components/comments-for-employee/comments-for-employee.component';
import { SelectionModel } from '@angular/cdk/collections';
import { State } from '../../shared/models/State';

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {
  pmEntries: Map<string, Array<ProjectManagementEntry>> = projectManagementResponseMock;
  displayedColumns = [
    'select',
    'employeeName',
    'projectCheckState',
    'employeeCheckState',
    'internalCheckState',
    'customerCheckState',
    'doneCommentsIndicator'
  ];
  pmSelectionModels: Array<SelectionModel<ProjectManagementEntry>>;

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
    if (this.pmEntries) {
      this.pmSelectionModels = [];
      for (let i = 0; i < this.pmEntries.size; i++) {
        this.pmSelectionModels.push(new SelectionModel<ProjectManagementEntry>(true, []));
      }
    }
  }

  areAllSelected(projectIndex: number, projectKey: string) {
    return this.pmSelectionModels[projectIndex].selected.length === this.pmEntries.get(projectKey).length;
  }

  masterToggle(projectIndex: number, projectKey: string) {
    this.areAllSelected(projectIndex, projectKey) ?
      this.pmSelectionModels[projectIndex].clear() :
      this.pmEntries.get(projectKey).forEach(row => this.pmSelectionModels[projectIndex].select(row));
  }

  openDialog(pmEntry: ProjectManagementEntry): void {
    const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
      {
        width: '100%',
        autoFocus: false,
      }
    );

    dialogRef.componentInstance.employee = pmEntry.employeeName;
    dialogRef.componentInstance.comments = pmEntry.comments;
  }

  isAnySelected(): boolean {
    return this.pmSelectionModels.filter(pmSelectionModel => pmSelectionModel.selected.length > 0).length > 0;
  }

  areAllProjectCheckStatesDone(projectKey: string): boolean {
    return this.pmEntries.get(projectKey).every(pmEntry => pmEntry.projectCheckState === State.DONE);
  }
}
