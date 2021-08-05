import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoatHttpComponent } from './boat-http.component';

describe('BoatHttpComponent', () => {
  let component: BoatHttpComponent;
  let fixture: ComponentFixture<BoatHttpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoatHttpComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoatHttpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
