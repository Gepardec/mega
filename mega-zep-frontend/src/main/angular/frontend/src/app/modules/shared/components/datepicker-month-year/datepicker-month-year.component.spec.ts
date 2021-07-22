import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DatepickerMonthYearComponent} from './datepicker-month-year.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from '@angular/forms';

describe('DatepickerMonthYearComponent', () => {
  let component: DatepickerMonthYearComponent;
  let fixture: ComponentFixture<DatepickerMonthYearComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule, BrowserAnimationsModule, ReactiveFormsModule],
      declarations: [DatepickerMonthYearComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatepickerMonthYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
