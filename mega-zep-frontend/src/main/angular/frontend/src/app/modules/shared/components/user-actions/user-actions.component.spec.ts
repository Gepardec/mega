import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {UserActionsComponent} from './user-actions.component';
import {RouterTestingModule} from '@angular/router/testing';
import {routes} from '../../../../app-routing.module';
import {AppModule} from '../../../../app.module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {User} from '../../models/User';
import {TranslateModule} from '@ngx-translate/core';
import {OAuthService} from 'angular-oauth2-oidc';
import {UserInfo} from 'angular-oauth2-oidc/types';

describe('UserActionsComponent', () => {
  let component: UserActionsComponent;
  let fixture: ComponentFixture<UserActionsComponent>;

  class OAuthServiceMock {
    loadUserProfile(): Promise<UserInfo> {
      return new Promise(() => Promise.resolve({sub: 'sub', picture: 'picture'}));
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot(),
        RouterTestingModule.withRoutes(routes),
        AppModule,
        HttpClientTestingModule,
        BrowserAnimationsModule
      ],
      declarations: [],
      providers: [
        {
          provide: OAuthService, useClass: OAuthServiceMock
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserActionsComponent);
    component = fixture.componentInstance;
    component.user = setupUser();
    fixture.detectChanges();
  });

  function setupUser(): User {
    const user: User = new User();
    user.email = 'max.mustermann@gmail.com';
    user.firstname = 'Max';
    user.lastname = 'Mustermann';
    return user;
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display first name and last name of user', () => {
    expect(fixture.debugElement.nativeElement.querySelector('#userBtn').textContent).toContain('Max Mustermann keyboard_arrow_down');
  });
});
