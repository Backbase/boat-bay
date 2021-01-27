import { Component, Input, OnInit } from '@angular/core';
import { BoatProduct, BoatSpec } from "../../models";

@Component({
  selector: 'app-spec-summary',
  templateUrl: './spec-summary.component.html',
  styleUrls: ['./spec-summary.component.scss']
})
export class SpecSummaryComponent implements OnInit {

  @Input() spec: BoatSpec | null = null;
  @Input() product: BoatProduct | null = null;

  constructor() { }

  ngOnInit(): void {
  }

}
