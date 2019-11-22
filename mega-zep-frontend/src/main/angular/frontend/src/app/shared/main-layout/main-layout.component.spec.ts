import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MainLayoutComponent} from './main-layout.component';
import {MainLayoutModule} from "./main-layout.module";
import {AppModule} from "../../app.module";
import {AuthenticationService} from "../../signin/authentication.service";
import {MockAuthenticationService} from "../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../signin/MockAuthService";

describe('MainLayoutComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MainLayoutModule,
        AppModule
      ],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService},
      ]
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<MainLayoutComponent> = TestBed.createComponent(MainLayoutComponent);
    const app: MainLayoutComponent = fixture.debugElement.componentInstance;


    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
