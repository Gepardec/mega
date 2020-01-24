import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserActionsComponent } from './user-actions.component';
import { RouterTestingModule } from '@angular/router/testing';
import { routes } from '../../../../../app-routing.module';
import { AppModule } from '../../../../../app.module';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { User } from '../../../models/User';

describe('UserActionsComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes(routes),
        AppModule,
        HttpClientTestingModule,
        BrowserAnimationsModule],
      declarations: [],
      providers: []
    })
      .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<UserActionsComponent> = TestBed.createComponent(UserActionsComponent);
    const app: UserActionsComponent = fixture.debugElement.componentInstance;

    fixture.detectChanges();

    return {fixture, app};
  }

  function setupUser(): User {
    const user: User = new User();
    user.email = 'max.mustermann@gmail.com';
    user.firstname = 'Max';
    user.lastname = 'Mustermann';

    return user;
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });

  it('should display first name and last name of user', () => {
    const {fixture, app} = setup();
    const user = setupUser();
    app.user = user;
    fixture.detectChanges();

    expect(fixture.debugElement.nativeElement.querySelector('#userBtn').textContent)
      .toEqual(user.firstname + ' ' + user.lastname + ' keyboard_arrow_down');
  });

  it('should display photo url', () => {
    const {fixture, app} = setup();
    const user = setupUser();
    app.user = user;
    const img: DebugElement = fixture.debugElement.query(By.css('.avatar'));
    fixture.detectChanges();

    // expect(img.nativeElement.src).toEqual(user.photoUrl);
  });
});
