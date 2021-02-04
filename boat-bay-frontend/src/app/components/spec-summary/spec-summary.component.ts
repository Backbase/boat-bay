import { Component, Input, OnInit } from '@angular/core';
import { BoatProduct, BoatSpec } from "../../models";

@Component({
  selector: 'app-spec-summary',
  templateUrl: './spec-summary.component.html',
  styleUrls: ['./spec-summary.component.scss']
})
export class SpecSummaryComponent{

  @Input() spec!: BoatSpec;
  @Input() product!: BoatProduct;


}
