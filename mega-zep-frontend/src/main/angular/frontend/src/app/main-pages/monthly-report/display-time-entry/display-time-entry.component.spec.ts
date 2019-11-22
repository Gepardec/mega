import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayTimeEntryComponent } from './display-time-entry.component';

describe('DisplayTimeEntryComponent', () => {
  let component: DisplayTimeEntryComponent;
  let fixture: ComponentFixture<DisplayTimeEntryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayTimeEntryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayTimeEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
