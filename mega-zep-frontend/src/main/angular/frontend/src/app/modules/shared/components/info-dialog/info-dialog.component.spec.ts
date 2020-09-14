import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoDialogComponent } from './info-dialog.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('InfoDialogComponent', () => {
  let component: InfoDialogComponent;
  let fixture: ComponentFixture<InfoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InfoDialogComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
