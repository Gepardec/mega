import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EmployeeCheckComponent} from './employee-check.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {CommentService} from '../../../shared/services/comment/comment.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {OAuthModule} from "angular-oauth2-oidc";

describe('EmployeeCheckComponent', () => {
  let component: EmployeeCheckComponent;
  let fixture: ComponentFixture<EmployeeCheckComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EmployeeCheckComponent],
      imports: [TranslateModule.forChild(), AngularMaterialModule, HttpClientTestingModule, RouterTestingModule, OAuthModule.forRoot()],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [CommentService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
