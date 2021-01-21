import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpecTagCloudComponent } from './spec-tag-cloud.component';

describe('TagCloudComponent', () => {
  let component: SpecTagCloudComponent;
  let fixture: ComponentFixture<SpecTagCloudComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpecTagCloudComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpecTagCloudComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
