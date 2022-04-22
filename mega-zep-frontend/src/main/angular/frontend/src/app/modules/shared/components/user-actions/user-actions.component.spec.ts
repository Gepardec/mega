import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {UserActionsComponent} from './user-actions.component';
import {User} from '../../models/User';
import {OAuthService} from 'angular-oauth2-oidc';
import {UserInfo} from 'angular-oauth2-oidc/types';
import {MatDialog} from '@angular/material/dialog';
import {AngularMaterialModule} from '../../../material/material-module';
import {TranslateModule} from '@ngx-translate/core';
import {expect} from '@angular/flex-layout/_private-utils/testing';
import {By} from '@angular/platform-browser';
import {MatButton} from '@angular/material/button';
import {click} from '../../../../testing/click-simulator';
import {MatMenuItem} from '@angular/material/menu';

describe('UserActionsComponent', () => {

  let fixture: ComponentFixture<UserActionsComponent>;
  let component: UserActionsComponent;

  let dialog: MatDialog;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        UserActionsComponent
      ],
      imports: [
        TranslateModule.forRoot(),
        AngularMaterialModule
      ],
      providers: [
        {provide: OAuthService, useClass: OAuthServiceMock}
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(UserActionsComponent);
      component = fixture.componentInstance;
      dialog = TestBed.inject(MatDialog);
    });
  }));


  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should display first name and last name of user', () => {
    component.user = UserMock.setupUser();
    fixture.detectChanges();

    expect(fixture.debugElement.nativeElement.querySelector('#userBtn').textContent).toContain(`${UserMock.lastname} keyboard_arrow_down`);
  });

  it('#menuTrigger - should open menu', () => {
    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeFalse();

    const triggerButton = fixture.debugElement.query(By.directive(MatButton));
    click(triggerButton);
    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeTrue();
  });

  it('#doLogout - should call logout on logout-button click', () => {
    spyOn(component.logout, 'emit');

    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeFalse();

    const triggerButton = fixture.debugElement.query(By.directive(MatButton));
    click(triggerButton);
    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeTrue();

    const logoutButton = fixture.debugElement.query(By.css('#logoutBtn'));
    click(logoutButton);
    fixture.detectChanges();

    expect(component.logout.emit).toHaveBeenCalled();
  });

  it('#openInfoDialog - should call dialog.open() on button click', () => {
    spyOn((component as any).dialog, 'open');

    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeFalse();

    const triggerButton = fixture.debugElement.query(By.directive(MatButton));
    click(triggerButton);
    fixture.detectChanges();

    expect(component.menuTrigger.menuOpen).toBeTrue();

    const infoDialogButton = fixture.debugElement.query(By.directive(MatMenuItem));
    click(infoDialogButton);
    fixture.detectChanges();

    expect((component as any).dialog.open).toHaveBeenCalled();
  });

  class UserMock {

    static email: string = 'max.mustermann@gmail.com';
    static firstname: string = 'Max';
    static lastname: string = 'Mustermann';

    static setupUser(): User {
      const user: User = new User();
      user.email = UserMock.email;
      user.firstname = UserMock.firstname;
      user.lastname = UserMock.lastname;

      return user;
    }
  }

  class OAuthServiceMock {
    loadUserProfile(): Promise<UserInfo> {
      return new Promise(() => Promise.resolve({sub: 'sub', picture: 'picture'}));
    }
  }
});
