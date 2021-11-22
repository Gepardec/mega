import {TestBed} from '@angular/core/testing';

import {CommentService} from './comment.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Comment} from "../../models/Comment";
import {State} from "../../models/State";
import * as _moment from 'moment';
import {ConfigService} from "../config/config.service";
import {Employee} from "../../models/Employee";
import {Step} from "../../models/Step";

const moment = _moment;


describe('CommentService', () => {
  let commentService: CommentService;
  let configService: ConfigService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    commentService = TestBed.inject(CommentService);
    configService = TestBed.inject(ConfigService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('#should be created', () => {
    expect(commentService).toBeTruthy();
  });

  it('#getDoneCommentsCount - should return right number of done comments', () => {
    expect(commentService.getDoneCommentsCount(CommentsMock.get()))
      .toEqual(2);
  });

  it('#getDoneComments - should return done comments', () => {
    expect(commentService.getDoneComments(CommentsMock.get()).map(comment => comment.id))
      .toEqual(CommentsMock.get().filter(comment => comment.state === State.DONE).map(comment => comment.id));
  });

  it('#setStatusDone - should return right id', (done) => {
    commentService.setStatusDone(CommentsMock.get().find(comment => comment.id === 1))
      .subscribe((id: number) => {
          expect(id).toEqual(1);
          done();
        },
        done.fail
      );

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/comments/setdone'));
    testRequest.flush(1);
  });

  it('#getCommentsForEmployee - should return comments for employee', () => {
    const firstComment = CommentsMock.get().find(comment => comment.id === 1);

    const date = moment().format(CommentsMock.dateFormat);
    commentService.getCommentsForEmployee(firstComment.authorEmail, date)
      .subscribe(comments => {
        expect(comments).not.toBeNull();
        expect(comments.length).toEqual(1);
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext(`/comments/getallcommentsforemployee?email=${firstComment.authorEmail}&date=${date}`));
    testRequest.flush([firstComment]);
  });

  it('#createNewComment - should return new comment', () => {
    const newComment = CommentsMock.createNew();

    commentService.createNewComment(new Employee(), newComment.message, newComment.authorEmail, Step.ACCEPT_TIMES, 'LIW-Microservices', moment().format(CommentsMock.dateFormat))
      .subscribe((comment: Comment) => {
        expect(comment).not.toBeNull();
        expect(comment.id).toEqual(4);
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/comments'));
    testRequest.flush(newComment);
  });

  it('#updateComment - should return updated comment', () => {
    const updatedComment = CommentsMock.get().find(comment => comment.id === 1);
    updatedComment.message = "new message";

    commentService.updateComment(updatedComment)
      .subscribe((comment: Comment) => {
        expect(comment).not.toBeNull();
        expect(comment.id).toEqual(1);
        expect(comment.message).toEqual('new message');
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/comments'));
    testRequest.flush(updatedComment);
  });

  it('#deleteComment - should return deleted comment', () => {
    const deletedComment = CommentsMock.get().find(comment => comment.id === 1);

    commentService.updateComment(deletedComment)
      .subscribe((comment: Comment) => {
        expect(comment).not.toBeNull();
        expect(comment.id).toEqual(1);
      });

    const testRequest = httpTestingController.expectOne(configService.getBackendUrlWithContext('/comments'));
    testRequest.flush(deletedComment);
  });

  class CommentsMock {

    static dateFormat: string = 'yyyy-MM-DD';

    static get(): Array<Comment> {
      return [
        {
          authorEmail: 'max@gepardec.com',
          authorName: 'max',
          id: 1,
          message: "text",
          isEditing: false,
          state: State.DONE,
          updateDate: moment.now().toString()
        },
        {
          authorEmail: 'susi@gepardec.com',
          authorName: 'susi',
          id: 2,
          message: "text",
          isEditing: false,
          state: State.DONE,
          updateDate: moment.now().toString()
        },
        {
          authorEmail: 'franz@gepardec.com',
          authorName: 'franz',
          id: 3,
          message: "text",
          isEditing: false,
          state: State.OPEN,
          updateDate: moment.now().toString()
        }
      ]
    }

    static createNew(): Comment {
      return {
        authorEmail: 'fredi@gepardec.com',
        authorName: 'fredi',
        id: 4,
        message: "text",
        isEditing: false,
        state: State.DONE,
        updateDate: moment.now().toString()
      }
    }
  }
});
