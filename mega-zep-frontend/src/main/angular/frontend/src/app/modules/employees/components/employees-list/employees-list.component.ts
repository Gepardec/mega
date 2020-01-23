import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Employee} from "../../models/Employee";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-employees-list',
  templateUrl: './employees-list.component.html',
  styleUrls: ['./employees-list.component.scss']
})
export class EmployeesListComponent implements OnInit, OnChanges {

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
