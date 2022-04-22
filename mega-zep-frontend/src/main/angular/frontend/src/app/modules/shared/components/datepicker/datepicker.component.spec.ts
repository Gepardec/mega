import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {DatepickerComponent} from './datepicker.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect} from '@angular/flex-layout/_private-utils/testing';

import * as _moment from 'moment';
import {configuration} from '../../constants/configuration';

const moment = _moment;
const DATE_FORMAT: string = configuration.dateFormat;

describe('DatepickerComponent', () => {

  let component: DatepickerComponent;
  let fixture: ComponentFixture<DatepickerComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        DatepickerComponent
      ],
      imports: [
        AngularMaterialModule,
        BrowserAnimationsModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(DatepickerComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterCreate - should create datePicker', () => {
    fixture.detectChanges();

    expect(component.datePicker).toBeTruthy();
  });

  it('#emitEvent - should call dateEmitter.emit()', () => {
    fixture.detectChanges();

    spyOn(component.dateEmitter, 'emit').and.stub();

    component.emitEvent(moment().format(DATE_FORMAT))

    expect(component.dateEmitter.emit).toHaveBeenCalled();
  });

  it('#getDateAndEmitEvent - should call dateEmitter.emit() with new value', () => {
    fixture.detectChanges();

    spyOn(component.dateEmitter, 'emit').and.stub();

    let event: any = {value: moment().subtract(1, 'month').format(DATE_FORMAT)};
    component.getDateAndEmitEvent(event);

    expect(component.dateEmitter.emit).toHaveBeenCalledWith(event.value);
  });
});

