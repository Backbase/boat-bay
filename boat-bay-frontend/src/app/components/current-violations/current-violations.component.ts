import { Component, Input, OnInit } from '@angular/core';
import { BoatStatistics } from "../../models/";

@Component({
  selector: 'current-violations',
  templateUrl: 'current-violations.component.html',
  styleUrls: ['current-violations.component.scss'],
})
export class CurrentViolationsComponent implements OnInit {
  @Input()
  statistics!: BoatStatistics;

  constructor() {}

  ngOnInit(): void {}
}
