import {TestBed} from '@angular/core/testing';

import {AuthenticationService} from './authentication.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {AuthService} from "angularx-social-login";
import {routes} from "../../app-routing.module";
import {MockAuthService} from "./MockAuthService";
import {AppModule} from "../../app.module";
import {NgZone} from "@angular/core";

describe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [
      AppModule,
      HttpClientTestingModule,
      RouterTestingModule.withRoutes(routes)],
    providers: [
      {provide: AuthService, useClass: MockAuthService}
    ]
  }));

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);

    expect(service).toBeTruthy();
  });
});
