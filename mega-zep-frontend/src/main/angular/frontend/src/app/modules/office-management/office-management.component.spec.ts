import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeManagementComponent } from './office-management.component';

describe('OfficeManagementComponent', () => {
  let component: OfficeManagementComponent;
  let fixture: ComponentFixture<OfficeManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OfficeManagementComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfficeManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
