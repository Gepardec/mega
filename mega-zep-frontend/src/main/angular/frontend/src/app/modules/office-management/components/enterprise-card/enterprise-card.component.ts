import {Component, OnDestroy, OnInit} from '@angular/core';
import * as _moment from 'moment';
import {Moment} from 'moment';
import {Subscription, zip} from 'rxjs';
import {OfficeManagementService} from '../../services/office-management.service';
import {tap} from 'rxjs/operators';
import {MatSelectChange} from '@angular/material/select';
import {Config} from '../../../shared/models/Config';
import {configuration} from '../../../shared/constants/configuration';
import {ConfigService} from '../../../shared/services/config/config.service';
import {EnterpriseEntriesService} from '../../services/enterprise-entries/enterprise-entries.service';
import {EnterpriseEntry} from '../../models/EnterpriseEntry';
import {EnterpriseStep} from '../../models/EnterpriseStep';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from '@angular/material/snack-bar';
import {TranslateService} from '@ngx-translate/core';
import {ProjectStateSelectComponent} from '../../../shared/components/project-state-select/project-state-select.component';

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
  officeManagementUrl: string;
  enterpriseEntry: EnterpriseEntry;
  EnterpriseStep = EnterpriseStep;
  tooltipShowDelay = 500;
  tooltipPosition = 'above';

  constructor(private configService: ConfigService,
              private omService: OfficeManagementService,
              private eeService: EnterpriseEntriesService,
              private _snackBar: MatSnackBar,
              private translate: TranslateService) {
  }

  get date() {
    return moment()
      .year(this.selectedYear)
      .month(this.selectedMonth)
      .date(1)
      .startOf('day');
  }

  ngOnInit(): void {
    this.configService.getConfig().subscribe((config: Config) => {
      this.officeManagementUrl = config.zepOrigin + '/' + configuration.OFFICE_MANAGEMENT_SEGMENT;
    });
    this.dateSelectionSub = zip(this.omService.selectedYear, this.omService.selectedMonth)
      .pipe(
        tap(value => {
          this.selectedYear = value[0];
          this.selectedMonth = value[1];
        })
      ).subscribe(() => {
        this.getEnterpriseEntry();
      });
  }

  ngOnDestroy(): void {
    if (this.dateSelectionSub) {
      this.dateSelectionSub.unsubscribe();
    }
  }

  dateChanged(date: Moment) {
    this.omService.selectedYear.next(moment(date).year());
    this.omService.selectedMonth.next(moment(date).month() + 1);
  }

  onChangeEnterpriseState($event: MatSelectChange, step: EnterpriseStep, projectStateSelect: ProjectStateSelectComponent) {
    let oldValue = this.enterpriseEntry[step];

    this.enterpriseEntry[step] = $event.value;
    console.log('Old value: ' + oldValue);
    console.log('New value: ' + this.enterpriseEntry[step]);

    this.eeService.updateEnterpriseEntry(this.enterpriseEntry, this.selectedYear, this.selectedMonth)
      .subscribe((success) => {
        if (success) {
        } else {
          this.showErrorSnackbar();
          this.enterpriseEntry[step] = oldValue;
          projectStateSelect.value = this.enterpriseEntry[step];
        }
        console.log('Current value after update: ' + this.enterpriseEntry[step]);
      });
  }

  private getEnterpriseEntry() {
    this.eeService.getEnterpriseEntry(this.selectedYear, this.selectedMonth)
      .subscribe((enterpriseEntry) => {
        this.enterpriseEntry = enterpriseEntry;
      });
  }

  private showErrorSnackbar() {
    this._snackBar.open(
      this.translate.instant('snackbar.message'),
      this.translate.instant('snackbar.confirm'),
      {
        horizontalPosition: <MatSnackBarHorizontalPosition>configuration.snackbar.horizontalPosition,
        verticalPosition: <MatSnackBarVerticalPosition>configuration.snackbar.verticalPosition,
        duration: configuration.snackbar.duration
      });
  }
}
