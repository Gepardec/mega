import {
  AfterViewInit, ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  NgZone,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';
import {take} from 'rxjs/operators';

@Component({
  selector: 'app-inline-text-editor',
  templateUrl: './inline-text-editor.component.html',
  styleUrls: ['./inline-text-editor.component.scss']
})
export class InlineTextEditorComponent implements OnInit, AfterViewInit {

  @Input() comment: string
  @Output() commentChange = new EventEmitter<string>();
  @ViewChild('autosize') autosize: CdkTextareaAutosize;
  @ViewChild('textarea') textarea: ElementRef;
  MAXIMUM_LETTERS = 500;

  constructor(private _ngZone: NgZone,
              private changeDectectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.textarea.nativeElement.focus();
    this.triggerResize();
    this.changeDectectorRef.detectChanges();
  }

  triggerResize() {
    this._ngZone.onStable.pipe(take(1))
      .subscribe(() => {
        this.autosize.resizeToFitContent(true);
      });
  }

  onCancel() {
    this.commentChange.emit(this.comment);
  }

  onSave(inputElement: HTMLTextAreaElement) {
    this.commentChange.emit(inputElement.value.trim());
  }
}
