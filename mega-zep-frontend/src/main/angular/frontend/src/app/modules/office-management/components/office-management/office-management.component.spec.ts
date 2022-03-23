import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {OfficeManagementComponent} from './office-management.component';
import {TranslateModule} from "@ngx-translate/core";
import {AngularMaterialModule} from "../../../material/material-module";
import {OfficeManagementModule} from "../../office-management.module";
import {RouterTestingModule} from "@angular/router/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('OfficeManagementComponent', () => {
  let component: OfficeManagementComponent;
  let fixture: ComponentFixture<OfficeManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [OfficeManagementModule, HttpClientTestingModule, TranslateModule.forChild(), AngularMaterialModule, RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
