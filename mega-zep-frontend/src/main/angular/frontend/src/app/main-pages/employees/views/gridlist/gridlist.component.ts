import {Component, Input, OnInit} from '@angular/core';
import {MitarbeiterResponseType} from "../../../../models/Mitarbeiter/MitarbeiterResponseType";
import {DisplayEmployeeListService} from "../../display-employee-list/display-employee-list.service";
import {MitarbeiterType} from "../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
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

  constructor(
    private displayEmployeeListService: DisplayEmployeeListService
  ) {
  }

  ngOnInit() {
  }

  releaseEmployee(employee: MitarbeiterType): void {
    let employees: Array<MitarbeiterType> = [];
    employees.push(employee);
    this.displayEmployeeListService.updateMitarbeiter(employees)
      .subscribe((res) => {
        console.log(res);
      });
  }

}
