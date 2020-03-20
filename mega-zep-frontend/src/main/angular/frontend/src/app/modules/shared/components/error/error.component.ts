import {Component, Injector, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ErrorService} from "../../services/error/error.service";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  errorMessage: string;
  private redirectUrl: string;

  constructor(
    private router: Router,
    private injector: Injector
  ) {
  }

  ngOnInit() {
    // TODO: using injector is bad practice, we should refactor this
    const errorService = this.injector.get(ErrorService);
    this.errorMessage = errorService.message;
    this.redirectUrl = errorService.redirectUrl;
    errorService.removeLastErrorData();
  }

  navigatePreviousPage() {
    this.router.navigate([this.redirectUrl]);
  }
}
