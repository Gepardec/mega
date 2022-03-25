import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {ErrorComponent} from './error.component';
import {RouterTestingModule} from '@angular/router/testing';
import {TranslateModule} from '@ngx-translate/core';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ErrorService} from "../../services/error/error.service";
import {expect} from "@angular/flex-layout/_private-utils/testing";
import {Router} from "@angular/router";
import {AngularMaterialModule} from "../../../material/material-module";

describe('ErrorComponent', () => {

  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;

  let errorService: ErrorService;
  let router: Router;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        ErrorComponent
      ],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        TranslateModule.forChild(),
        AngularMaterialModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ErrorComponent);
      component = fixture.componentInstance;

      errorService = TestBed.inject(ErrorService);
      router = TestBed.inject(Router);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should call errorService.removeLastErrorData()', () => {
    fixture.detectChanges();

    spyOn(errorService, 'removeLastErrorData').and.stub();

    component.ngOnInit();

    expect(errorService.removeLastErrorData).toHaveBeenCalled();
  });

  it('#navigatePreviousPage - should call router.navigate()', () => {
    fixture.detectChanges();

    spyOn(router, 'navigate').and.stub();

    component.navigatePreviousPage();

    expect(router.navigate).toHaveBeenCalled();
  });
});
