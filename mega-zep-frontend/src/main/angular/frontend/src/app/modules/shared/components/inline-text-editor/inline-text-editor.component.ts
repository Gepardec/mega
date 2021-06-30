import {Component, EventEmitter, Input, NgZone, OnInit, Output, ViewChild} from '@angular/core';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';
import {take} from 'rxjs/operators';

@Component({
  selector: 'app-inline-text-editor',
  templateUrl: './inline-text-editor.component.html',
  styleUrls: ['./inline-text-editor.component.scss']
})
export class InlineTextEditorComponent implements OnInit {

  @Input() comment: string
  @Output() commentChange = new EventEmitter<string>();
  @ViewChild('autosize') autosize: CdkTextareaAutosize;

  constructor(private _ngZone: NgZone) { }

  ngOnInit(): void {
  }

  triggerResize() {
    this._ngZone.onStable.pipe(take(1))
      .subscribe(() => this.autosize.resizeToFitContent(true));
  }

  onCancel() {
    this.commentChange.emit(this.comment);
  }

  onSave(inputElement: HTMLTextAreaElement) {
    this.commentChange.emit(inputElement.value);
  }
}
