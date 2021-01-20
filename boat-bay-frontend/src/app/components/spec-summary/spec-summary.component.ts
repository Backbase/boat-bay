import { Component, Input, OnInit } from '@angular/core';
import { BoatSpec } from "../../models";

@Component({
  selector: 'app-spec-summary',
  templateUrl: './spec-summary.component.html',
  styleUrls: ['./spec-summary.component.scss']
})
export class SpecSummaryComponent implements OnInit {

  @Input() spec!: BoatSpec

  constructor() { }

  ngOnInit(): void {
  }

}
