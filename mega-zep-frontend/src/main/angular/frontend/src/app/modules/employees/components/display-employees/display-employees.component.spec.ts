import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayEmployeesComponent } from './display-employees.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { EmployeesService } from '../../services/employees.service';
import { NotificationService } from '../../../shared/services/notification/notification.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Employee } from '../../models/Employee';
import { SelectionChange } from '@angular/cdk/collections';

describe('DisplayEmployeesComponent', () => {
  let component: DisplayEmployeesComponent;
  let fixture: ComponentFixture<DisplayEmployeesComponent>;

  class EmployeesServiceMock {
    selectedEmployees: BehaviorSubject<Array<Employee>> = new BehaviorSubject<Array<Employee>>([]);

    getEmployees(): Observable<Array<Employee>> {
      return new BehaviorSubject<Array<Employee>>([]);
    }

    setSelectedEmployees(value: SelectionChange<Employee>): void {

    }

    setResetSelection(value: boolean): void {

    }
  }

  class NotificationServiceMock {

  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DisplayEmployeesComponent],
      providers: [
        {
          provide: EmployeesService, useClass: EmployeesServiceMock
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
    fixture = TestBed.createComponent(DisplayEmployeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
