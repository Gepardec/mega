import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {MatTableDataSource} from "@angular/material";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit, OnChanges {

  @Input('isGridlistActive') isGridlistActive: boolean;

  @Input('employees') employees: Array<MitarbeiterType>;
  public dataSource = new MatTableDataSource<MitarbeiterType>();

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions = [5, 10, 20, 50, 100];

  constructor() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.updateDatasource();
  }

  ngOnInit() {
    this.updateDatasource();
  }

  updateList(event): void {
    if (this.isGridlistActive) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
      this.dataSource.data = this.employees
        .slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
    } else {
      this.dataSource.data = this.employees;
    }

  }

  updateDatasource() {
    if (this.isGridlistActive) {
      this.dataSource.data = this.employees
        .slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
    } else {
      this.dataSource.data = this.employees;
    }
  }
}
