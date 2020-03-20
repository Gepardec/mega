import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesTableListComponent } from './employees-table-list.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { EmployeesService } from '../../services/employees.service';
import { BehaviorSubject } from 'rxjs';

describe('EmployeesTableListComponent', () => {
  let component: EmployeesTableListComponent;
  let fixture: ComponentFixture<EmployeesTableListComponent>;

  class EmployeesServiceMock {
    get resetSelection(): BehaviorSubject<boolean> {
      return new BehaviorSubject<boolean>(true);
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeesTableListComponent],
      imports: [
        MatTableModule
      ],
      providers: [
        {
          provide: EmployeesService, useClass: EmployeesServiceMock
        }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeesTableListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
