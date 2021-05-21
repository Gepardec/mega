import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatSelect, MatSelectChange} from "@angular/material/select";
import {ProjectState} from "../../models/ProjectState";

@Component({
  selector: 'app-project-state-select',
  templateUrl: './project-state-select.component.html',
  styleUrls: ['./project-state-select.component.scss']
})
export class ProjectStateSelectComponent implements OnInit {

  @ViewChild('select') select: MatSelect;
  ProjectState = ProjectState;
  @Input() value;
  @Output() selectionChange = new EventEmitter<MatSelectChange>();

  constructor() {
  }

  ngOnInit(): void {
  }

  get isDisabled(): boolean {
    return this.value === ProjectState.DONE || this.value === ProjectState.NOT_RELEVANT
      || this.select?.value === ProjectState.DONE || this.select?.value === ProjectState.NOT_RELEVANT;
  }

  onSelectionChange(selectChange: MatSelectChange): void {
    this.selectionChange.emit(selectChange);
    // this.value = ProjectState.DONE;
  }

}
