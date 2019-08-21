import {Component, Input, OnInit} from '@angular/core';
import {DisplayMitarbeiterListeService} from "./display-mitarbeiter-liste.service";
import {MitarbeiterResponseType} from "../models/Mitarbeiter/MitarbeiterResponseType";
import {SocialUser} from "angularx-social-login";
import {AuthenticationService} from "../signin/authentication.service";

@Component({
  selector: 'app-display-mitarbeiter-liste',
  templateUrl: './display-mitarbeiter-liste.component.html',
  styleUrls: ['./display-mitarbeiter-liste.component.scss']
})
export class DisplayMitarbeiterListeComponent implements OnInit {

  @Input('SocialUser') user: SocialUser;

  mitarbeiter: MitarbeiterResponseType;

  constructor(
    private displayMitarbeiterListeService: DisplayMitarbeiterListeService,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() {
    this.authenticationService.currentUser.subscribe((user: SocialUser) => {
      this.user = user;
      this.displayMitarbeiterListeService.getMitarbeiter(this.user)
        .subscribe((mitarbeiter: MitarbeiterResponseType) => {
          this.mitarbeiter = mitarbeiter;
        });
    });

  }

}
