import { Component, Input, OnInit } from '@angular/core';
import {BoatSpec} from "../../services/dashboard/model/boatSpec";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";

@Component({
  selector: 'app-spec-summary',
  templateUrl: './spec-summary.component.html',
  styleUrls: ['./spec-summary.component.scss']
})
export class SpecSummaryComponent{

  @Input() spec!: BoatSpec;
  @Input() product!: BoatProduct;


}
