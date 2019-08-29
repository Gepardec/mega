import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayEmployeeComponent } from './display-employee.component';

describe('DisplayEmployeeComponent', () => {
  let component: DisplayEmployeeComponent;
  let fixture: ComponentFixture<DisplayEmployeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayEmployeeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
