import {Component, OnDestroy, OnInit} from '@angular/core';
import * as _moment from 'moment';
import {OfficeManagementService} from '../../services/office-management.service';
import {Subscription, zip} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Moment} from 'moment';

const moment = _moment;

@Component({
  selector: 'app-office-management',
  templateUrl: './office-management.component.html',
  styleUrls: ['./office-management.component.scss']
})
export class OfficeManagementComponent implements OnInit, OnDestroy {

  selectedYear: number;
  selectedMonth: number;
  dateSelectionSub: Subscription;

  constructor(private omService: OfficeManagementService) {
  }

  ngOnInit(): void {
    this.dateSelectionSub = zip(this.omService.selectedYear, this.omService.selectedMonth)
      .pipe(
        tap(value => {
          this.selectedYear = value[0];
          this.selectedMonth = value[1];
        })
      ).subscribe();
  }

  ngOnDestroy(): void {
    console.log('OfficeManagementComponent destroyed');
    this.omService.selectedYear.next(moment().subtract(1, 'month').year());
    this.omService.selectedMonth.next(moment().subtract(1, 'month').month() + 1);
    if (this.dateSelectionSub) {
      this.dateSelectionSub.unsubscribe();
    }
  }

  dateChanged(date: Moment) {
    this.omService.selectedYear.next(moment(date).year());
    this.omService.selectedMonth.next(moment(date).month() + 1);
  }

  get date() {
    return moment()
      .year(this.selectedYear)
      .month(this.selectedMonth)
      .date(1)
      .startOf('day');
  }
}
