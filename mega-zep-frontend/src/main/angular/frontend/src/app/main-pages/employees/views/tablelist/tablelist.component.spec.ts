import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TablelistComponent} from './tablelist.component';
import {AngularMaterialModule} from "../../../../material-module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {Employee} from "../../../../models/Employee/Employee";
import {SelectionModel} from "@angular/cdk/collections";
import {MatTableDataSource} from "@angular/material";

describe('TablelistComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule, HttpClientTestingModule],
      declarations: [TablelistComponent]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<TablelistComponent> = TestBed.createComponent(TablelistComponent);
    const app: TablelistComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  function setEmployees(): Array<Employee> {
    let employees: Array<Employee> = new Array<Employee>();

    let empl1 = new Employee();
    let empl2 = new Employee();
    let empl3 = new Employee();

    empl1.vorname = "Max";
    empl1.nachname = "Mustermann";
    empl1.abteilung = "Mitarbeiter";
    empl1.freigabedatum = "2019-01-01";
    employees.push(empl1);

    empl2.vorname = "Susi";
    empl2.nachname = "Musterfrau";
    empl2.abteilung = "Sekretariat";
    empl2.freigabedatum = "2019-05-07";
    employees.push(empl2);

    empl3.vorname = "Rosa";
    empl3.nachname = "Roester";
    empl3.abteilung = "Softwareentwickler";
    empl3.freigabedatum = "2019-07-02";
    employees.push(empl3);

    return employees;
  }

  function setupList(app: TablelistComponent): Array<Employee> {
    app.selection = new SelectionModel<Employee>(true, null);
    app.dataSource = new MatTableDataSource<Employee>();
    let employees: Array<Employee> = setEmployees();
    app.dataSource.data = employees;

    return employees;
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should select all', () => {
    const {fixture, app} = setup();

    let employees: Array<Employee> = setupList(app);
    employees.forEach(empl => app.selection.toggle(empl));
    expect(app.isAllSelected()).toEqual(true);
  });

  it('should master toggle all', () => {
    const {fixture, app} = setup();

    setupList(app);

    app.masterToggle();
    expect(app.isAllSelected()).toEqual(true);

    app.masterToggle();
    expect(app.isAllSelected()).toEqual(false);
  });
});
