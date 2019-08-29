import {Component, Input, OnInit} from '@angular/core';
import {MitarbeiterResponseType} from "../../../models/Mitarbeiter/MitarbeiterResponseType";
import {MatTableDataSource} from "@angular/material";
import {MitarbeiterType} from "../../../models/Mitarbeiter/Mitarbeiter/MitarbeiterType";

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  @Input('isGridlistActive') isGridlistActive: boolean = true;

  @Input('employees') employees: MitarbeiterResponseType;
  public dataSource = new MatTableDataSource<MitarbeiterType>();

  pageIndex = 0;
  pageSize = 10;
  pageSizeOptions = [5, 10, 20, 50, 100];


  constructor() {
  }

  ngOnInit() {
    this.dataSource.data = this.employees.mitarbeiterListe.mitarbeiter
      .slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
  }

  updateList(event): void {
    console.log(event);
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.dataSource.data = this.employees.mitarbeiterListe.mitarbeiter
      .slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
  }


}
