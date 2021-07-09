import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EnterpriseCardComponent} from './enterprise-card.component';
import {OfficeManagementModule} from "../../office-management.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('EnterpriseCardComponent', () => {
  let component: EnterpriseCardComponent;
  let fixture: ComponentFixture<EnterpriseCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [OfficeManagementModule, HttpClientTestingModule]
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
