import {TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {AuthService} from "angularx-social-login";
import {routes} from "../app-routing.module";
import {MainLayoutModule} from "../shared/main-layout/main-layout/main-layout.module";
import {GoogleSigninComponent} from "./google-signin/google-signin.component";

fdescribe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
      providers: [
        {provide: AuthService}
      ],
      declarations: [GoogleSigninComponent],
      imports: [MainLayoutModule,
        RouterTestingModule.withRoutes(routes),
        HttpClientTestingModule,
      ]
    }).compileComponents()
  );

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    expect(service).toBeTruthy();
  });
});
