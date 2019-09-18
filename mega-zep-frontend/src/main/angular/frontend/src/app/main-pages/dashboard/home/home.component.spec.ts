import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import {DisplayEmployeeListComponent} from "../../employees/display-employee-list/display-employee-list.component";

describe('HomeComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeComponent ]
    })
    .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DisplayEmployeeListComponent> = TestBed.createComponent(DisplayEmployeeListComponent);
    const app: DisplayEmployeeListComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    let {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
