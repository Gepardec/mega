import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { OAuthService } from 'angular-oauth2-oidc';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from '../../services/user/user.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  class OAuthServiceMock {

  }

  class UserServiceMock {

    loggedInWithGoogle(): boolean {
      return true;
    }
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule
      ],
      providers: [
        {
          provide: OAuthService, useClass: OAuthServiceMock
        },
        {
          provide: UserService, useClass: UserServiceMock
        }
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
