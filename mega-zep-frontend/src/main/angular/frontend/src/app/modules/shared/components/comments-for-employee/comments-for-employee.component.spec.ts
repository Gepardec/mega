import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentsForEmployeeComponent } from './comments-for-employee.component';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMaterialModule } from '../../../material/material-module';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('CommentsForEmployeeComponent', () => {
  let component: CommentsForEmployeeComponent;
  let fixture: ComponentFixture<CommentsForEmployeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CommentsForEmployeeComponent],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentsForEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
