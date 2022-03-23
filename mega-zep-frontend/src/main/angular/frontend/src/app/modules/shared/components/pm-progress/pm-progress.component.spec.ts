import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PmProgressComponent} from './pm-progress.component';
import {AngularMaterialModule} from "../../../material/material-module";
import {TranslateModule} from "@ngx-translate/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {SharedModule} from "../../shared.module";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {MatDialogRef} from "@angular/material/dialog";
import {MAT_BOTTOM_SHEET_DATA} from "@angular/material/bottom-sheet";

describe('EmployeeProgressComponent', () => {
  let component: PmProgressComponent;
  let fixture: ComponentFixture<PmProgressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PmProgressComponent],
      imports: [
        BrowserAnimationsModule,
        SharedModule,
        AngularMaterialModule,
        TranslateModule.forChild(),
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        {provide: MAT_BOTTOM_SHEET_DATA, useValue: {}},
        {provide: MatDialogRef, useValue: {}}
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PmProgressComponent);
    component = fixture.componentInstance;
    component.pmProgresses = []

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
