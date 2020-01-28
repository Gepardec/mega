import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatePickerDialogComponent } from './date-picker-dialog.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { NotificationService } from '../../../../shared/services/notification/notification.service';
import { EmployeeService } from '../../../services/employee.service';

describe('DatePickerDialogComponent', () => {
  let component: DatePickerDialogComponent;
  let fixture: ComponentFixture<DatePickerDialogComponent>;

  class EmployeeServiceMock {

  }

  class NotificationServiceMock {

  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DatePickerDialogComponent],
      imports: [
        MatDialogModule
      ],
      providers: [
        {
          provide: MAT_DIALOG_DATA, useValue: {}
        },
        {
          provide: EmployeeService, useClass: EmployeeServiceMock
        },
        {
          provide: NotificationService, useClass: NotificationServiceMock
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatePickerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
