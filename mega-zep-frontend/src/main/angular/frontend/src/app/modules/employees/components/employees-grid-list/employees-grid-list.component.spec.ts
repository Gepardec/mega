import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeesGridListComponent } from './employees-grid-list.component';

describe('EmployeesGridListComponent', () => {
  let component: EmployeesGridListComponent;
  let fixture: ComponentFixture<EmployeesGridListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmployeesGridListComponent ]
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
