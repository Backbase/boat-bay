import { Component, Inject, Input } from '@angular/core';
import { BoatProduct, BoatViolation } from "../../models/";
import { JIRA_BASE_URL } from "../../app.constants";

@Component({
  selector: 'app-create-jira-issue-button',
  templateUrl: './create-jira-issue-button.component.html',
  styleUrls: ['./create-jira-issue-button.component.scss']
})
export class CreateJiraIssueButtonComponent  {

  @Input()
  public violation!:BoatViolation

  @Input()
  public product!: BoatProduct

  constructor(@Inject(JIRA_BASE_URL) public jiraBaseUrl: string) { }


}
