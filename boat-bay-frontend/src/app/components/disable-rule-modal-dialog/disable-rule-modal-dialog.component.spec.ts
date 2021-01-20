import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisableRuleModalDialogComponent } from './disable-rule-modal-dialog.component';

describe('DisableRuleModalDialogComponent', () => {
  let component: DisableRuleModalDialogComponent;
  let fixture: ComponentFixture<DisableRuleModalDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DisableRuleModalDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DisableRuleModalDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
