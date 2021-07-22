import {Component, OnInit} from '@angular/core';
import {Info} from '../../models/Info';
import {InfoService} from '../../services/info/info.service';

@Component({
  selector: 'app-info-dialog',
  templateUrl: './info-dialog.component.html',
  styleUrls: ['./info-dialog.component.scss']
})
export class InfoDialogComponent implements OnInit {

  info: Info;

  constructor(private infoService: InfoService) {
  }

  ngOnInit(): void {
    this.infoService.getInfo().subscribe(info => this.info = info);
  }

}
