import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GridlistComponent} from './gridlist.component';
import {AngularMaterialModule} from "../../../../material-module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {MitarbeiterType} from "../../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
import {MatDialogConfig, MatDialogRef} from "@angular/material";
import {DatePickerDialogComponent} from "./date-picker-dialog/date-picker-dialog.component";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {EmployeesPagesModule} from "../../home/home.module";

describe('GridlistComponent', () => {

  let employee: MitarbeiterType = new MitarbeiterType();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AngularMaterialModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        EmployeesPagesModule],
      declarations: [],
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<GridlistComponent> = TestBed.createComponent(GridlistComponent);
    const app: GridlistComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  function setEmployee() {
    employee.vorname = "Max";
    employee.nachname = "Mustermann";
    employee.email = "max.mustermann@mustermann.at";
    employee.freigabedatum = "2019-01-01";
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should open dialog', () => {
    const {fixture, app} = setup();
    setEmployee();
    let dialog: MatDialogRef<DatePickerDialogComponent, MatDialogConfig> = app.openDialog(employee);
    expect(dialog).not.toBeNull();
    dialog.close();
  });
});
