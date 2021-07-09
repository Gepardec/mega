import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {InlineTextEditorComponent} from './inline-text-editor.component';
import {SharedModule} from "../../shared.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

describe('InlineTextEditorComponent', () => {
  let component: InlineTextEditorComponent;
  let fixture: ComponentFixture<InlineTextEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, BrowserAnimationsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InlineTextEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
