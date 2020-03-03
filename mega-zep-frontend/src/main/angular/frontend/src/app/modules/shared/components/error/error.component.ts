import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  private message: string;
  private previousUrl: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    // FIXME GAJ: get params from service not from pathparam
    this.route.params.subscribe(params => {this.message = params["errorMessage"]; this.previousUrl = params["previousPage"];});
  }

  navigatePreviousPage() {
    this.router.navigate([this.previousUrl]);
  }
}
