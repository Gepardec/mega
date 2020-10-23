import { Component, Input, OnInit } from '@angular/core';
import { MonthlyReport } from '../../models/MonthlyReport';
import { CommentService } from '../../../shared/services/comment/comment.service';
import { State } from '../../../shared/models/State';
import { MatSelectionListChange } from '@angular/material/list';

@Component({
  selector: 'app-employee-check',
  templateUrl: './employee-check.component.html',
  styleUrls: ['./employee-check.component.scss']
})
export class EmployeeCheckComponent implements OnInit {
  @Input() monthlyReport: MonthlyReport;
  State = State;

  constructor(public commentService: CommentService) {
  }

  ngOnInit(): void {
  }

  selectionChange(change: MatSelectionListChange): void {
    const comment = change.option.value;
    const selected = change.option.selected;
    if (selected) {
      // TODO: Server request
    }
  }

}
