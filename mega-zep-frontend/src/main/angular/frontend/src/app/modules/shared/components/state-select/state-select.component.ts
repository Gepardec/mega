import {AfterViewChecked, ChangeDetectorRef, Component, EventEmitter, Input, Output} from '@angular/core';
import {State} from '../../models/State';
import {MatSelectChange} from '@angular/material/select';

@Component({
  selector: 'app-state-select',
  templateUrl: './state-select.component.html',
  styleUrls: ['./state-select.component.scss']
})
export class StateSelectComponent implements AfterViewChecked {

  State = State;

  @Input() value: State;
  @Output() selectionChange = new EventEmitter<MatSelectChange>();

  constructor(private cdr: ChangeDetectorRef) {
  }

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  onSelectionChange(selectChange: MatSelectChange): void {
    this.selectionChange.emit(selectChange);
    this.value = State.DONE;
  }
}
