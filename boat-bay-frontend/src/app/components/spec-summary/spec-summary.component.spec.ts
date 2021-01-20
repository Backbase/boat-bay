import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecSummaryComponent } from './spec-summary.component';

describe('SpecSummaryComponent', () => {
  let component: SpecSummaryComponent;
  let fixture: ComponentFixture<SpecSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpecSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
