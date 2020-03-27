import {Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  message: string;
  redirectUrl: string;

  storeLastErrorData(message: string, redirectPage: string) {
    this.message = message;
    this.redirectUrl = redirectPage;
  }

  removeLastErrorData() {
    delete this.message;
    delete this.message;
  }

  getErrorMessage(error: Error): string {
    if (error instanceof HttpErrorResponse) {
      // Server Error
      return this.getServerMessage(error);
    }
    // Client Error
    return this.getClientMessage(error);
  }

  getClientMessage(error: Error): string {
    if (!navigator.onLine) {
      return 'No Internet Connection';
    }
    return error.message ? error.message : error.toString();
  }

  getClientStack(error: Error): string {
    return error.stack;
  }

  getServerMessage(error: HttpErrorResponse): string {
    return error.message;
  }

  getServerStack(error: HttpErrorResponse): string {
    // handle stack trace
    return error.error;
  }
}
