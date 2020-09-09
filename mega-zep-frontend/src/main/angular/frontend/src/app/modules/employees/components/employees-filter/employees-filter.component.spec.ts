import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesFilterComponent } from './employees-filter.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { EmployeesService } from '../../services/employees.service';
import { NotificationService } from '../../../shared/services/notification/notification.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Employee } from '../../../shared/models/Employee';
import { SelectionChange } from '@angular/cdk/collections';
import { TranslateModule } from '@ngx-translate/core';

describe('EmployeesFilterComponent', () => {
  let component: EmployeesFilterComponent;
  let fixture: ComponentFixture<EmployeesFilterComponent>;

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
      declarations: [EmployeesFilterComponent],
      imports: [
        TranslateModule.forRoot()
      ],
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
    fixture = TestBed.createComponent(EmployeesFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
