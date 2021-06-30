import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InlineTextEditorComponent } from './inline-text-editor.component';

describe('InlineTextEditorComponent', () => {
  let component: InlineTextEditorComponent;
  let fixture: ComponentFixture<InlineTextEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InlineTextEditorComponent ]
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
