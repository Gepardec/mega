import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CalenderComponent} from './calender.component';
import {AngularMaterialModule} from "../../../material-module";

describe('CalenderComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule],
      declarations: [ CalenderComponent ]
    })
    .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<CalenderComponent> = TestBed.createComponent(CalenderComponent);
    const app: CalenderComponent = fixture.debugElement.componentInstance;
    fixture.detectChanges();
    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
