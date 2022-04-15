import {ComponentFixture, fakeAsync, flush, TestBed, waitForAsync} from '@angular/core/testing';

import {InlineTextEditorComponent} from './inline-text-editor.component';
import {SharedModule} from '../../shared.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgZone} from '@angular/core';
import {expect} from '@angular/flex-layout/_private-utils/testing';

describe('InlineTextEditorComponent', () => {

  let component: InlineTextEditorComponent;
  let fixture: ComponentFixture<InlineTextEditorComponent>;

  let ngZone: NgZone;

  beforeEach(waitForAsync((() => {
    TestBed.configureTestingModule({
      declarations: [
        InlineTextEditorComponent
      ],
      imports: [
        SharedModule,
        BrowserAnimationsModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(InlineTextEditorComponent);
      component = fixture.componentInstance;

      ngZone = TestBed.inject(NgZone);
    });
  })));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#afterInit - should call focus and triggerResize', () => {
    fixture.detectChanges();

    spyOn(component, 'triggerResize').and.stub();
    spyOn(component.textarea.nativeElement, 'focus').and.stub();

    component.ngAfterViewInit();

    expect(component.textarea.nativeElement.focus).toHaveBeenCalled();
    expect(component.triggerResize).toHaveBeenCalled();
  });

  it('#triggerResize - should call autosize.resizeToFitContent', fakeAsync(() => {
    fixture.detectChanges();

    spyOn(component.autosize, 'resizeToFitContent').and.callThrough();

    component.triggerResize();
    fixture.detectChanges();
    flush();

    expect(component.autosize.resizeToFitContent).toHaveBeenCalledWith(true);
  }));

  it('#onCancel - should call commentChange.emit()', () => {
    fixture.detectChanges();

    spyOn(component.commentChange, 'emit').and.stub();

    component.onCancel(createMockEvent('1'), null);

    expect(component.commentChange.emit).toHaveBeenCalled();
  });

  it('#onCancel - should call onSave', () => {
    fixture.detectChanges();

    spyOn(component, 'onSave').and.stub();

    component.onCancel(createMockEvent('savebutton'), null);

    expect(component.onSave).toHaveBeenCalled();
  });

  const createMockEvent = (id: string) => {
    return {
      relatedTarget: {
        id: id
      }
    }
  }
});
