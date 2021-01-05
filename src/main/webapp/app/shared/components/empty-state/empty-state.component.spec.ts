import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { By } from '@angular/platform-browser';
import { EmptyStateComponent } from './empty-state.component';

@Component({
  template: ` <app-empty-state>Test empty state content</app-empty-state> `,
})
class TestHostComponent {}

describe('EmptyStateComponent', () => {
  let component: EmptyStateComponent;
  let fixture: ComponentFixture<EmptyStateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EmptyStateComponent, TestHostComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestHostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show correct text content', () => {
    const textElement = fixture.debugElement.query(By.css('mat-card-subtitle'));

    expect(textElement.nativeElement.innerText.trim()).toBe('Test empty state content');
  });
});
