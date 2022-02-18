import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {StateIndicatorComponent} from './state-indicator.component';
import {AngularMaterialModule} from '../../../material/material-module';
import {State} from "../../models/State";
import {By} from "@angular/platform-browser";
import {expect} from "@angular/flex-layout/_private-utils/testing";

describe('StateIndicatorComponent', () => {

  let fixture: ComponentFixture<StateIndicatorComponent>;
  let component: StateIndicatorComponent;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        StateIndicatorComponent
      ],
      imports: [
        AngularMaterialModule
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(StateIndicatorComponent);
      component = fixture.componentInstance;
    });
  }));

  it('#should create', () => {
    expect(component).toBeTruthy();
  });

  it('#stateOpen - should show cancel icon', () => {
    fixture.detectChanges();

    component.state = State.OPEN;
    fixture.detectChanges();

    const matIcon = fixture.debugElement.query(By.css('.red'));
    expect(matIcon.nativeElement.textContent).toEqual('cancel');
  });

  it('#stateUnknown - should show error icon', () => {
    fixture.detectChanges();

    component.state = "unknown";
    fixture.detectChanges();

    const matIcon = fixture.debugElement.query(By.css('.yellow'));
    expect(matIcon.nativeElement.textContent).toEqual('error');
  });
});
