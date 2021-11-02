import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ErrorService} from '../../services/error/error.service';

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
    private errorService: ErrorService
  ) {
  }

  ngOnInit() {
    this.errorMessage = this.errorService.message;
    this.redirectUrl = this.errorService.redirectUrl;
    this.errorService.removeLastErrorData();
  }

  navigatePreviousPage() {
    this.router.navigate([this.redirectUrl]);
  }
}
