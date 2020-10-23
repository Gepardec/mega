import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { State } from '../../models/State';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-state-select',
  templateUrl: './state-select.component.html',
  styleUrls: ['./state-select.component.scss']
})
export class StateSelectComponent implements OnInit {
  State = State;
  @Input() value;
  @Output() selectionChange = new EventEmitter<MatSelectChange>();

  constructor() {
  }

  ngOnInit(): void {
  }

  onSelectionChange(selectChange: MatSelectChange): void {
    this.selectionChange.emit(selectChange);
  }
}
