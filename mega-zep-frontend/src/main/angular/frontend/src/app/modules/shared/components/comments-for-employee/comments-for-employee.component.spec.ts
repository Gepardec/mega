import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {CommentsForEmployeeComponent} from './comments-for-employee.component';
import {TranslateModule} from '@ngx-translate/core';
import {AngularMaterialModule} from '../../../material/material-module';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {OAuthModule} from 'angular-oauth2-oidc';
import {MatDialogRef} from '@angular/material/dialog';
import {Comment} from "../../models/Comment";
import {State} from "../../models/State";

import * as _moment from "moment";
import {UserService} from "../../services/user/user.service";
import {User} from "../../models/User";
import {expect} from "@angular/flex-layout/_private-utils/testing";
import {CommentService} from "../../services/comment/comment.service";
import {of} from "rxjs";
import {Employee} from "../../models/Employee";
import {ElementRef} from "@angular/core";
import {configuration} from "../../constants/configuration";

const moment = _moment;
const DATE_FORMAT: string = configuration.dateFormat;

describe('CommentsForEmployeeComponent', () => {

  let component: CommentsForEmployeeComponent;
  let fixture: ComponentFixture<CommentsForEmployeeComponent>;

  let userService: UserService;
  let commentService: CommentService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        CommentsForEmployeeComponent
      ],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        RouterTestingModule,
        OAuthModule.forRoot()
      ],
      providers: [
        {provide: MatDialogRef, useClass: MatDialogRefMock},
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(CommentsForEmployeeComponent);
      component = fixture.componentInstance;

      userService = TestBed.inject(UserService);
      commentService = TestBed.inject(CommentService);
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#onInit - should set all comments to editing=false and subscribe on userService.user', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(userService.user, 'subscribe').and.stub();

    component.comments = CommentsMock.setupComments();

    component.ngOnInit();
    flush();

    component.comments.forEach(comment => {
      expect(comment.isEditing).toBeFalse();
    })

    expect(userService.user.subscribe).toHaveBeenCalled();
  }));

  it('#toggleIsEditing - should toggle isEditing', () => {
    fixture.detectChanges();

    const comment = CommentsMock.setupComments()[0];

    expect(comment.isEditing).toBeTrue();

    component.toggleIsEditing(comment);

    expect(comment.isEditing).toBeFalse();
  });

  it('#editCommentBtnVisible - should return true', () => {
    fixture.detectChanges();

    const comment = CommentsMock.setupComments()[0];
    comment.isEditing = false;
    component.user = UserMock.setupUser();

    const isCommentBtnVisible = component.editCommentBtnVisible(comment);

    expect(isCommentBtnVisible).toBeTrue();
  });

  it('#isReleased - should return true', () => {
    fixture.detectChanges();

    const releaseDate = moment().subtract(1, 'month').format(DATE_FORMAT);

    const isReleased = component.isReleased(releaseDate);

    expect(isReleased).toBeTrue();
  });

  it('#parseAnchorTags - should replace http with href', () => {
    fixture.detectChanges();

    const url = 'http://localhost:8080'
    const replacedText = component.parseAnchorTags(url);

    expect(replacedText).toContain('href=');
  });

  it('#parseAnchorTags - should replace www with href', () => {
    fixture.detectChanges();

    const url = 'wwww.gepardec.com'
    const replacedText = component.parseAnchorTags(url);

    expect(replacedText).toContain('href=');
  });

  it('#parseAnchorTags - should replace email with href="mailto', () => {
    fixture.detectChanges();

    const email = UserMock.setupUser().email;
    const replacedText = component.parseAnchorTags(email);

    expect(replacedText).toContain('href="mailto');
  });

  it('#createCommentForEmployee - should call commentHasChanged.emit and commentService.getCommentsForEmployee', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(commentService, 'createNewComment').and.returnValue(of(null));
    spyOn(commentService, 'getCommentsForEmployee').and.returnValue(of(CommentsMock.setupComments()));
    spyOn(component.commentHasChanged, 'emit').and.stub();

    component.user = UserMock.setupUser();
    component.employee = new Employee();
    component.employee.email = UserMock.setupUser().email;

    component.createCommentForEmployee(CommentsMock.setupComments()[0].message);
    flush();

    expect(component.commentHasChanged.emit).toHaveBeenCalled();
    expect(commentService.getCommentsForEmployee).toHaveBeenCalled();
  }));

  it('#updateCommentForEmployee - should call commentHasChanged.emit', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(commentService, 'updateComment').and.returnValue(of(null));
    spyOn(component.commentHasChanged, 'emit').and.stub();


    component.updateCommentForEmployee(CommentsMock.setupComments()[0]);
    flush();

    expect(component.commentHasChanged.emit).toHaveBeenCalled();
  }));

  it('#deleteCommentOfEmployee - should remove comment', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(commentService, 'deleteComment').and.returnValue(of(null));
    spyOn(component.commentHasChanged, 'emit').and.stub();

    component.comments = CommentsMock.setupComments();

    component.deleteCommentOfEmployee(CommentsMock.setupComments()[0]);
    flush();

    expect(component.commentHasChanged.emit).toHaveBeenCalled();
    expect(component.comments.filter(comment => {
      return comment.id === CommentsMock.setupComments()[0].id
    }).length).toBe(0);
  }));

  it('#close - should call dialogRef.close(true)', () => {
    fixture.detectChanges();


    component.newCommentTextarea = new ElementRef({value: ''});

    spyOn(component.dialogRef, 'close').and.stub();
    //spyOn(component, 'newCommentTextarea').and.returnValue(textarea);

    component.close();

    expect(component.dialogRef.close).toHaveBeenCalledWith(true);
  });

  it('#close - should call dialog.open and dialogRef.afterClosed', fakeAsync(() => {
    fixture.detectChanges();


    component.newCommentTextarea = new ElementRef({value: 'hello'});

    spyOn(component.dialog, 'open').and.returnValue(component.dialogRef);
    spyOn(component.dialogRef, 'afterClosed').and.returnValue(of(false));

    component.close();
    flush();

    expect(component.dialog.open).toHaveBeenCalled();
    expect(component.dialogRef.afterClosed).toHaveBeenCalled();
  }));

  it('#close - should call dialog.open and dialogRef.afterClosed and dialogRef.close', fakeAsync(() => {
    fixture.detectChanges();

    component.newCommentTextarea = new ElementRef({value: 'hello'});

    spyOn(component.dialog, 'open').and.returnValue(component.dialogRef);
    spyOn(component.dialogRef, 'close').and.stub();
    spyOn(component.dialogRef, 'afterClosed').and.returnValue(of(true));

    component.close();
    flush();

    expect(component.dialog.open).toHaveBeenCalled();
    expect(component.dialogRef.afterClosed).toHaveBeenCalled();
    expect(component.dialogRef.close).toHaveBeenCalled();
  }));

  class CommentsMock {

    static setupComments(): Array<Comment> {
      return [
        {
          id: 1,
          message: "Hello",
          state: State.OPEN,
          authorEmail: "max.mustermann@gepardec.com",
          updateDate: moment().format(DATE_FORMAT),
          isEditing: true,
          authorName: "Max Mustermann"
        },
        {
          id: 1,
          message: "World",
          state: State.OPEN,
          authorEmail: "max.mustermann@gepardec.com",
          updateDate: moment().format(DATE_FORMAT),
          isEditing: true,
          authorName: "Max Mustermann"
        }
      ]
    }
  }

  class UserMock {

    static email: string = 'max.mustermann@gepardec.com';
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

  class MatDialogRefMock {
    close(): void {
    }

    afterClosed(): void {
    }
  }
});
