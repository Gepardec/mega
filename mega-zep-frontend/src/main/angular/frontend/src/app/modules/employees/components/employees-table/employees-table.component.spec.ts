import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesTableComponent } from './employees-table.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { BehaviorSubject } from 'rxjs';
import { EmployeesService } from '../../services/employees.service';
import { TranslateModule } from '@ngx-translate/core';

describe('EmployeesTableListComponent', () => {
  let component: EmployeesTableComponent;
  let fixture: ComponentFixture<EmployeesTableComponent>;

  class EmployeesServiceMock {
    get resetSelection(): BehaviorSubject<boolean> {
      return new BehaviorSubject<boolean>(true);
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeesTableComponent],
      imports: [
        MatTableModule,
        TranslateModule.forRoot()
      ],
      providers: [
        {provide: EmployeesService, useClass: EmployeesServiceMock}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
