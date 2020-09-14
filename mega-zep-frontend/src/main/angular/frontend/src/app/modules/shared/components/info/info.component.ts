import { Component, OnInit } from '@angular/core';
import { InfoService } from '../../services/info/info.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {

  version: string;

  constructor(private infoService: InfoService) {
  }

  ngOnInit(): void {
    this.infoService.getInfo().subscribe(info => this.version = info.version);
  }
}
