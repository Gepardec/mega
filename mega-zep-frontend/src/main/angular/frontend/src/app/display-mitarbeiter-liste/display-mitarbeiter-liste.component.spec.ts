import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DisplayMitarbeiterListeComponent } from './display-mitarbeiter-liste.component';

describe('DisplayMitarbeiterListeComponent', () => {
  let component: DisplayMitarbeiterListeComponent;
  let fixture: ComponentFixture<DisplayMitarbeiterListeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DisplayMitarbeiterListeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DisplayMitarbeiterListeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
