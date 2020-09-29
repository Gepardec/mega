import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StateIndicatorComponent } from './state-indicator.component';
import { AngularMaterialModule } from '../../../material/material-module';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('StateIndicatorComponent', () => {
  let component: StateIndicatorComponent;
  let fixture: ComponentFixture<StateIndicatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [StateIndicatorComponent],
      imports: [AngularMaterialModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StateIndicatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
