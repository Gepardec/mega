import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PaginationComponent} from './pagination.component';
import {AngularMaterialModule} from "../../../material-module";
import {EmployeesPagesModule} from "../home/home.module";
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";
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
    const fixture: ComponentFixture<PaginationComponent> = TestBed.createComponent(PaginationComponent);
    const app: PaginationComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  function setEmployees(empl: MitarbeiterResponseType): MitarbeiterResponseType {
    let emplType = new MitarbeiterType();
    emplType.vorname = "Max";
    emplType.nachname = "Mustermann";
    emplType.email = "max.mustermann@mustermail.com";
    empl.mitarbeiterTypeList = new Array<MitarbeiterType>();
    empl.length = 1;
    empl.mitarbeiterTypeList = new Array<MitarbeiterType>(emplType);
    return empl;
  }

  function shouldDisplayView(app: PaginationComponent, fixture: ComponentFixture<PaginationComponent>,
                             viewId: string, isGridlistInitiallyActive: boolean) {
    app.employees = Array<MitarbeiterType>();
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
