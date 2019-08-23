import {Component, OnInit} from '@angular/core';
import {MainLayoutService} from "../../main-layout/main-layout/main-layout.service";
import {AuthenticationService} from "../../../signin/authentication.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(
    private mainLayoutService: MainLayoutService,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
  }

}
