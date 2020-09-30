import { Component, OnInit } from '@angular/core';
import { ProjectManagementEntry } from '../models/ProjectManagementEntry';
import projectManagementResponseMock from '../models/MockData';
import { MatDialog } from '@angular/material/dialog';
import { CommentsForEmployeeComponent } from '../../shared/components/comments-for-employee/comments-for-employee.component';
import { SelectionModel } from '@angular/cdk/collections';

@Component({
  selector: 'app-project-management',
  templateUrl: './project-management.component.html',
  styleUrls: ['./project-management.component.scss']
})
export class ProjectManagementComponent implements OnInit {
  projectManagementResponse: Map<string, Array<ProjectManagementEntry>> = projectManagementResponseMock;
  displayedColumns = [
    'employeeName',
    'projectCheckState',
    'employeeCheckState',
    'internalCheckState',
    'customerCheckState',
    'doneCommentsIndicator'
  ];
  selection = new SelectionModel<ProjectManagementEntry>(true, []);

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  openDialog(pmEntry: ProjectManagementEntry): void {
    const dialogRef = this.dialog.open(CommentsForEmployeeComponent,
      {
        width: '100%',
        autoFocus: false
      }
    );

    dialogRef.componentInstance.employee = pmEntry.employeeName;
    dialogRef.componentInstance.comments = pmEntry.comments;
  }
}
