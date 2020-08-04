import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatepickerComponent } from './datepicker.component';
import { AngularMaterialModule } from '../../../../material-module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('DatepickerComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule, BrowserAnimationsModule],
      declarations: [DatepickerComponent]
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<DatepickerComponent> = TestBed.createComponent(DatepickerComponent);
    const app: DatepickerComponent = fixture.debugElement.componentInstance;
    fixture.detectChanges();
    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});

