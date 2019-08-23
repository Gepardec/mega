import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayEmployeeListComponent } from './display-employee-list.component';

describe('DisplayMitarbeiterListeComponent', () => {
  let component: DisplayEmployeeListComponent;
  let fixture: ComponentFixture<DisplayEmployeeListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayEmployeeListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayEmployeeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
