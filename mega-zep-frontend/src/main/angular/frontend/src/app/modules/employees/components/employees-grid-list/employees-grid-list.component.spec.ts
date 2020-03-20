import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesGridListComponent } from './employees-grid-list.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { MatDialogModule } from '@angular/material/dialog';

describe('EmployeesGridListComponent', () => {
  let component: EmployeesGridListComponent;
  let fixture: ComponentFixture<EmployeesGridListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeesGridListComponent],
      imports: [
        MatDialogModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeesGridListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
