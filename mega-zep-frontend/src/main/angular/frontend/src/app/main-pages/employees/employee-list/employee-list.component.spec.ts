import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EmployeeListComponent} from './employee-list.component';
import {AngularMaterialModule} from "../../../material-module";
import {EmployeesPagesModule} from "../home/home.module";
import {EmployeeResponseType} from "../../../models/Employee/EmployeeResponseType";
import {Employee} from "../../../models/Employee/Employee";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('PaginationComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        HttpClientTestingModule,
        AngularMaterialModule,
        EmployeesPagesModule
      ],
      declarations: []
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<EmployeeListComponent> = TestBed.createComponent(EmployeeListComponent);
    const app: EmployeeListComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  function setEmployees(empl: EmployeeResponseType): EmployeeResponseType {
    let emplType = new Employee();
    emplType.vorname = "Max";
    emplType.nachname = "Mustermann";
    emplType.email = "max.mustermann@mustermail.com";
    empl.employeeList = new Array<Employee>();
    empl.length = 1;
    empl.employeeList = new Array<Employee>(emplType);
    return empl;
  }

  function shouldDisplayView(app: EmployeeListComponent, fixture: ComponentFixture<EmployeeListComponent>,
                             viewId: string, isGridlistInitiallyActive: boolean) {
    app.employees = Array<Employee>();
    //FIXME GAJ: test has compile errors check later
    //setEmployees(app.employees);

    app.isGridlistActive = isGridlistInitiallyActive;
    fixture.detectChanges();
    let list = fixture.debugElement.nativeElement.querySelector(viewId);
    expect(list).not.toBeNull();

    app.isGridlistActive = !isGridlistInitiallyActive;
    fixture.detectChanges();
    list = fixture.debugElement.nativeElement.querySelector(viewId);
    expect(list).toBeNull();
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should display Gridlist', () => {
    const {fixture, app} = setup();
    shouldDisplayView(app, fixture, "#app-gridlist", true);
  });

  it('should display Tablelist', () => {
    const {fixture, app} = setup();
    shouldDisplayView(app, fixture, "#app-tablelist", false);
  });
});
