import { Component, OnInit, TemplateRef } from '@angular/core';
import { InfoService } from '../../services/info/info.service';
import { Info } from '../../models/Info';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {

  info: Info;

  constructor(private infoService: InfoService, private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.infoService.getInfo().subscribe(info => this.info = info);
  }

  openInfoDetailsInDialog(infoDetails: TemplateRef<any>): void {
    this.dialog.open(infoDetails, {minWidth: '50%'});
  }
}
