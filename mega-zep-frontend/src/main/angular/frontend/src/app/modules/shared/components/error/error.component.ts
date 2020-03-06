import {Component, Injector, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ErrorService} from "../../services/error/error.service";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  private message: string;
  private previousUrl: string;

  constructor(
    private router: Router,
    private injector: Injector
  ) { }

  ngOnInit() {
    const errorService = this.injector.get(ErrorService);
    this.message = errorService.message;
    this.previousUrl = errorService.redirectPage;
    errorService.removeLastErrorData();
  }

  navigatePreviousPage() {
    this.router.navigate([this.previousUrl]);
  }
}
