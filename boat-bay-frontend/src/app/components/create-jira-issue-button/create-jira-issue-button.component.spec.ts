import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateJiraIssueButtonComponent } from './create-jira-issue-button.component';

describe('CreateJiraIssueButtonComponent', () => {
  let component: CreateJiraIssueButtonComponent;
  let fixture: ComponentFixture<CreateJiraIssueButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateJiraIssueButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateJiraIssueButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
