import {Component, Input, OnInit} from '@angular/core';
import {MitarbeiterResponseType} from "../../../../models/Mitarbeiter/MitarbeiterResponseType";
import {configuration} from "../../../../../configuration/configuration";

@Component({
  selector: 'app-gridlist',
  templateUrl: './gridlist.component.html',
  styleUrls: ['./gridlist.component.scss']
})
export class GridlistComponent implements OnInit {

  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  @Input('employees') employees: MitarbeiterResponseType;
  @Input('pageSize') pageSize: number;
  @Input('pageIndex') pageIndex: number;

  constructor() {
  }

  ngOnInit() {
  }

}
