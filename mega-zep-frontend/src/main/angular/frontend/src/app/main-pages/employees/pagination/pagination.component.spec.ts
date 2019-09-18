import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PaginationComponent} from './pagination.component';
import {AngularMaterialModule} from "../../../material-module";
import {EmployeesPagesModule} from "../home/home.module";

describe('PaginationComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
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

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
