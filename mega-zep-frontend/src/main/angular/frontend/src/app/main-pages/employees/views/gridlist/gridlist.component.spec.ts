import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {GridlistComponent} from './gridlist.component';
import {AngularMaterialModule} from "../../../../material-module";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('GridlistComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AngularMaterialModule, HttpClientTestingModule],
      declarations: [GridlistComponent]
    }).compileComponents();
  }));

  function setup() {
    const fixture: ComponentFixture<GridlistComponent> = TestBed.createComponent(GridlistComponent);
    const app: GridlistComponent = fixture.debugElement.componentInstance;

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
