import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeManagementComponent } from './office-management.component';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMaterialModule } from '../../material/material-module';
import { SharedModule } from '../../shared/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('OfficeManagementComponent', () => {
  let component: OfficeManagementComponent;
  let fixture: ComponentFixture<OfficeManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OfficeManagementComponent],
      imports: [
        BrowserAnimationsModule,
        SharedModule,
        AngularMaterialModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        RouterTestingModule
      ]
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
