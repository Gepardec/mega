import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MonthlyReport} from '../../models/MonthlyReport';
import {CommentService} from '../../../shared/services/comment/comment.service';
import {State} from '../../../shared/models/State';
import {MatSelectionListChange} from '@angular/material/list';
import {ErrorHandlerService} from '../../../shared/services/error/error-handler.service';
import {StepentriesService} from '../../../shared/services/stepentries/stepentries.service';
import {Step} from '../../../shared/models/Step';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {PmProgressComponent} from '../../../shared/components/pm-progress/pm-progress.component';
import {configuration} from "../../../shared/constants/configuration";

@Component({
  selector: 'app-employee-check',
  templateUrl: './employee-check.component.html',
  styleUrls: ['./employee-check.component.scss']
})
export class EmployeeCheckComponent implements OnInit {

  @Input() monthlyReport: MonthlyReport;
  @Output() refreshMonthlyReport: EventEmitter<void> = new EventEmitter<void>();
  State = State;

  employeeProgressRef: MatBottomSheetRef;

  constructor(
    public commentService: CommentService,
    public errorHandlerService: ErrorHandlerService,
    public stepentriesService: StepentriesService,
    private _bottomSheet: MatBottomSheet) {
  }

  ngOnInit(): void {
  }

  selectionChange(change: MatSelectionListChange): void {
    const comment = change.option.value;

    this.commentService.setStatusDone(comment).subscribe((updated: number) => {
      const updatedComment = this.monthlyReport.comments.find(value => value.id === comment.id);
      updatedComment.state = State.DONE;

      const allCommentDone: boolean = this.monthlyReport.comments.every(c => c.state === State.DONE);
      if (allCommentDone) {
        this.emitRefreshMonthlyReport();
      }
    });
  }

  setOpenAndUnassignedStepEntriesDone() {
    this.stepentriesService
      .close(this.monthlyReport.employee, Step.CONTROL_TIMES, this.monthlyReport.employee.releaseDate)
      .subscribe((success: boolean) => {
        this.emitRefreshMonthlyReport();
      });
  }

  emitRefreshMonthlyReport() {
    this.refreshMonthlyReport.emit();
  }

  openEmployeeProgress() {
    this.employeeProgressRef = this._bottomSheet.open(PmProgressComponent, {
      data: {employeeProgresses: this.monthlyReport.employeeProgresses},
      autoFocus: false,
      hasBackdrop: false
    });
  }

  closeEmployeeProgress() {
    this.employeeProgressRef.dismiss();
  }

  parseBody(body: string) {
    const urlPattern = /https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,4}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/igm;
    return body.replace(urlPattern, '<a href=\$& target="_blank"\>$&</a>');
  }
}
