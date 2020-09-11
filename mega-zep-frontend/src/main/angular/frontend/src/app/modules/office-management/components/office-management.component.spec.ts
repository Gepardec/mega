import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeManagementComponent } from './office-management.component';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMaterialModule } from '../../material/material-module';
import { SharedModule } from '../../shared/shared.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

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
        TranslateModule.forRoot()
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
