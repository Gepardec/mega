import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {NavigationModule} from "../navigation.module";
import {MainLayoutModule} from "../../main-layout/main-layout.module";
import {AppModule} from "../../../app.module";
import {AuthenticationService} from "../../../signin/authentication.service";
import {MockAuthenticationService} from "../../../signin/MockAuthenticationService";
import {AuthService} from "angularx-social-login";
import {MockAuthService} from "../../../signin/MockAuthService";

describe('HeaderComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        NavigationModule,
        MainLayoutModule,
        AppModule
      ],
      declarations: [],
      providers: [
        {provide: AuthenticationService, useClass: MockAuthenticationService},
        {provide: AuthService, useClass: MockAuthService}
      ]
    });
  }));

  function setup() {
    const fixture: ComponentFixture<HeaderComponent> = TestBed.createComponent(HeaderComponent);
    const app: HeaderComponent = fixture.debugElement.componentInstance;
    fixture.detectChanges();

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
