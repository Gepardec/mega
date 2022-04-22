import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-done-comments-indicator',
  templateUrl: './done-comments-indicator.component.html',
  styleUrls: ['./done-comments-indicator.component.scss']
})
export class DoneCommentsIndicatorComponent {

  @Input() totalComments: number;
  @Input() finishedComments: number;

}
