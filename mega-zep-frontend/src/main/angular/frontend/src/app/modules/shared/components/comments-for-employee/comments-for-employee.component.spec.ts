import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentsForEmployeeComponent } from './comments-for-employee.component';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMaterialModule } from '../../../material/material-module';

describe('CommentsForEmployeeComponent', () => {
  let component: CommentsForEmployeeComponent;
  let fixture: ComponentFixture<CommentsForEmployeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CommentsForEmployeeComponent],
      imports: [
        AngularMaterialModule,
        TranslateModule.forRoot()
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentsForEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
