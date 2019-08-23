import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GridlistComponent } from './gridlist.component';

describe('GridlistComponent', () => {
  let component: GridlistComponent;
  let fixture: ComponentFixture<GridlistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GridlistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GridlistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
