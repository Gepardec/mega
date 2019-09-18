import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TablelistComponent } from './tablelist.component';
import {GridlistComponent} from "../gridlist/gridlist.component";
import {AngularMaterialModule} from "../../../../material-module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('TablelistComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule, HttpClientTestingModule],
      declarations: [ TablelistComponent ]
    })
    .compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<TablelistComponent> = TestBed.createComponent(TablelistComponent);
    const app: TablelistComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
