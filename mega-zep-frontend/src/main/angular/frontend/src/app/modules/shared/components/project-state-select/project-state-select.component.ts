import {
  AfterViewChecked,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild
} from '@angular/core';
import {MatSelect, MatSelectChange} from '@angular/material/select';
import {ProjectState} from '../../models/ProjectState';

@Component({
  selector: 'app-project-state-select',
  templateUrl: './project-state-select.component.html',
  styleUrls: ['./project-state-select.component.scss']
})
export class ProjectStateSelectComponent implements AfterViewChecked {

  ProjectState = ProjectState;

  @Input() value: ProjectState;
  @Output() selectionChange = new EventEmitter<MatSelectChange>();
  @ViewChild('select') select: MatSelect;

  constructor(private cdr: ChangeDetectorRef) {
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

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  onSelectionChange(selectChange: MatSelectChange): void {
    this.selectionChange.emit(selectChange);
  }
}
