import {Component, OnInit} from '@angular/core';
import {configuration} from "../../../../configuration/configuration";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  readonly pages = configuration.PAGES;

  constructor() {
  }

  ngOnInit() {
  }

}
