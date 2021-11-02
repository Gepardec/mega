import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DoneCommentsIndicatorComponent} from './done-comments-indicator.component';
import {CommentService} from '../../services/comment/comment.service';
import {CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DoneCommentsIndicatorComponent', () => {
  let component: DoneCommentsIndicatorComponent;
  let fixture: ComponentFixture<DoneCommentsIndicatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DoneCommentsIndicatorComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      imports: [HttpClientTestingModule],
      providers: [CommentService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DoneCommentsIndicatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
