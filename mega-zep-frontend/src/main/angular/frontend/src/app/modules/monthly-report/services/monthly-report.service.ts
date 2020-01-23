import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { MonthlyReport } from "../models/MonthlyReport";
import { Observable } from "rxjs";
import { ConfigService } from "../../shared/services/config/config.service";

@Injectable({
  providedIn: 'root'
})
export class MonthlyReportService {

  constructor(
    private httpClient: HttpClient,
    private config: ConfigService) {
  }

  getAll(): Observable<MonthlyReport> {
    return this.httpClient.post<MonthlyReport>(this.config.getBackendUrl() + '/worker/employee/monthendReport', null);
  }
}
