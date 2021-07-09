import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {MatSelect, MatSelectChange} from '@angular/material/select';
import {ProjectState} from '../../models/ProjectState';
import {ProjectManagementService} from '../../../project-management/services/project-management.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-project-state-select',
  templateUrl: './project-state-select.component.html',
  styleUrls: ['./project-state-select.component.scss']
})
export class ProjectStateSelectComponent implements OnInit {

  @ViewChild('select') select: MatSelect;
  ProjectState = ProjectState;
  @Input() value
  @Output() selectionChange = new EventEmitter<MatSelectChange>();

  constructor() {
  }

  ngOnInit(): void {
  }

  get isInProgressSelected(): boolean {
    return this.value === ProjectState.WORK_IN_PROGRESS;
  }

  get isNotRelevantSelected(): boolean {
    return this.value === ProjectState.NOT_RELEVANT;
  }

  get isDoneSelected(): boolean {
    return this.value === ProjectState.DONE;
  }

  onSelectionChange(selectChange: MatSelectChange): void {
    this.selectionChange.emit(selectChange);
  }
}
