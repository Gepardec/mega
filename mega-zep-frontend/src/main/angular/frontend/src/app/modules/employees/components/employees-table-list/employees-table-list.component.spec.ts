import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesTableListComponent } from './employees-table-list.component';

describe('EmployeesTableListComponent', () => {
  let component: EmployeesTableListComponent;
  let fixture: ComponentFixture<EmployeesTableListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeesTableListComponent ]
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
