import { Component, Inject, Input } from '@angular/core';
import { JIRA_BASE_URL } from "../../app.constants";
import {BoatViolation} from "../../services/dashboard/model/boatViolation";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";

@Component({
  selector: 'app-create-jira-issue-button',
  templateUrl: './create-jira-issue-button.component.html',
  styleUrls: ['./create-jira-issue-button.component.scss']
})
export class CreateJiraIssueButtonComponent  {

  @Input()
  public violation!:BoatViolation

  @Input()
  public product: BoatProduct| null = null;

  constructor(@Inject(JIRA_BASE_URL) public jiraBaseUrl: string) { }


}
