import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainPagesContainer } from './main-pages.container';

describe('MainPagesComponent', () => {
  let component: MainPagesContainer;
  let fixture: ComponentFixture<MainPagesContainer>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MainPagesContainer ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainPagesContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
