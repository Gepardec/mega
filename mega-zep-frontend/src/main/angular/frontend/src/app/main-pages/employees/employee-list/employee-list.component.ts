import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {MatTableDataSource} from "@angular/material";
import {Employee} from "../../../models/Employee/Employee";

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit, OnChanges {

  @Input('isGridlistActive') isGridlistActive: boolean;

  @Input('employees') employees: Array<Employee>;
  public dataSource = new MatTableDataSource<Employee>();

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
