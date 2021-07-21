import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BillableTimesComponent} from './billable-times.component';

describe('BillableTimesComponent', () => {
  let component: BillableTimesComponent;
  let fixture: ComponentFixture<BillableTimesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BillableTimesComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BillableTimesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
