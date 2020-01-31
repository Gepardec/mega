import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {AppModule} from '../../../../app.module';

describe('HeaderComponent', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        AppModule
      ],
      declarations: [],
      providers: [

      ]
    });
  }));

  function setup() {
    const fixture: ComponentFixture<HeaderComponent> = TestBed.createComponent(HeaderComponent);
    const app: HeaderComponent = fixture.debugElement.componentInstance;
    fixture.detectChanges();

    return {fixture, app};
  }

  it('should create', () => {
    const {fixture, app} = setup();
    expect(app).toBeTruthy();
  });
});
