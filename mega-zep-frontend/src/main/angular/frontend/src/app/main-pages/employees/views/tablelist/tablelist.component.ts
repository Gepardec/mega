import {Component, Input, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material";
import {MitarbeiterType} from "../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {SelectionChange, SelectionModel} from "@angular/cdk/collections";
import {DisplayEmployeeListService} from "../../display-mitarbeiter-liste/display-employee-list.service";
import {configuration} from "../../../../../configuration/configuration";

@Component({
  selector: 'app-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.scss']
})
export class TablelistComponent implements OnInit {

  readonly functions = configuration.EMPLOYEE_FUNCTIONS;

  displayedColumns = ['nachname', 'abteilung', 'freigabedatum', 'auswaehlen'];
  @Input('dataSource') dataSource: MatTableDataSource<MitarbeiterType>;
  selection = new SelectionModel<MitarbeiterType>(true, null);

  constructor(
    private displayMitarbeiterService: DisplayEmployeeListService
  ) {
  }

  ngOnInit() {
    this.selection.changed.subscribe(
      (selectedEmployees: SelectionChange<MitarbeiterType>) => {
        this.displayMitarbeiterService.setSelectedEmployees(selectedEmployees);
      }
    );
  }


}
