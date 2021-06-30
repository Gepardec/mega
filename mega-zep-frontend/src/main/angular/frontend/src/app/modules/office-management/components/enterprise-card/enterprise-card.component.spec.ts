import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EnterpriseCardComponent } from './enterprise-card.component';

describe('EnterpriseCardComponent', () => {
  let component: EnterpriseCardComponent;
  let fixture: ComponentFixture<EnterpriseCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnterpriseCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnterpriseCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
