import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesContainer } from './employees.container';

describe('EmployeesComponent', () => {
  let component: EmployeesContainer;
  let fixture: ComponentFixture<EmployeesContainer>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeesContainer ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeesContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
