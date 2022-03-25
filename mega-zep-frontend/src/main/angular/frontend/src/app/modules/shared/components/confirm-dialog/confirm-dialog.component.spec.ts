import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ConfirmDialogComponent} from './confirm-dialog.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {MatDialogRef} from '@angular/material/dialog';
import {TranslateModule} from "@ngx-translate/core";

describe('ConfirmDialogComponent', () => {

  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        ConfirmDialogComponent
      ],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot()
        TranslateModule.forChild(),
        HttpClientTestingModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ],
      providers: [
        {provide: MatDialogRef, useClass: MatDialogRefMock},
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ConfirmDialogComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#onConfirm - should call dialogRef.close() with true', () => {
    fixture.detectChanges();

    spyOn(component.dialogRef, 'close').and.stub();

    component.onConfirm();

    expect(component.dialogRef.close).toHaveBeenCalledOnceWith(true);
  });

  it('#onDismiss - should call dialogRef.close() with false', () => {
    fixture.detectChanges();

    spyOn(component.dialogRef, 'close').and.stub();

    component.onDismiss();

    expect(component.dialogRef.close).toHaveBeenCalledOnceWith(false);
  });

  class MatDialogRefMock {

    close(): void {
    }
  }
});
