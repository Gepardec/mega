import {Component, Input, OnInit} from '@angular/core';
import {MitarbeiterResponseType} from "../../../../models/Mitarbeiter/MitarbeiterResponseType";
import {MitarbeiterType} from "../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {configuration} from "../../../../../configuration/configuration";
import {DisplayEmployeeListService} from "../../display-employee-list/display-employee-list.service";
import {MatDialog, MatDialogConfig} from "@angular/material";
import {DatePickerDialogComponent} from "./date-picker-dialog/date-picker-dialog.component";

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

  filteredEmployees: Array<MitarbeiterType>;

  constructor(
    private displayEmployeeListService: DisplayEmployeeListService,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
    if (this.employees.mitarbeiterListe && this.employees.mitarbeiterListe.mitarbeiter) {
      this.filteredEmployees = this.employees.mitarbeiterListe.mitarbeiter;
    }
  }

  releaseEmployee(employee: MitarbeiterType): void {
    let employees: Array<MitarbeiterType> = [];
    employees.push(employee);
    this.displayEmployeeListService.updateEmployees(employees, null)
      .subscribe((res) => {
      });
  }

  openDialog(employee: MitarbeiterType): void {
    let config: MatDialogConfig = new MatDialogConfig();
    config.data = employee;
    const dialogRef = this.dialog.open(DatePickerDialogComponent, config);


    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog closed ${result}`);
    });
  }

  applyFilter(filterValue: string) {
    this.filteredEmployees = this.employees.mitarbeiterListe.mitarbeiter
      .filter(empl => empl.vorname.toLowerCase().includes(filterValue.toLowerCase())
        || empl.nachname.toLowerCase().includes(filterValue.toLowerCase()));
  }


}
