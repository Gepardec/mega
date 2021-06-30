import {Component, OnDestroy, OnInit} from '@angular/core';
import * as _moment from 'moment';
import {Moment} from 'moment';
import {Subscription, zip} from 'rxjs';
import {OfficeManagementService} from '../../services/office-management.service';
import {tap} from 'rxjs/operators';
import {FormControl} from '@angular/forms';

const moment = _moment;

@Component({
  selector: 'app-enterprise-card',
  templateUrl: './enterprise-card.component.html',
  styleUrls: ['./enterprise-card.component.scss']
})
export class EnterpriseCardComponent implements OnInit, OnDestroy {

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
    console.log('EnterpriseCardComponent destroyed');
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
