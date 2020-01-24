import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatePickerDialogComponent } from './date-picker-dialog.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { UtilService } from '../../../../shared/util/util.service';
import { NotificationService } from '../../../../shared/services/notification/notification.service';

describe('DatePickerDialogComponent', () => {
  let component: DatePickerDialogComponent;
  let fixture: ComponentFixture<DatePickerDialogComponent>;

  class UtilServiceMock {

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
          provide: UtilService, useClass: UtilServiceMock
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
