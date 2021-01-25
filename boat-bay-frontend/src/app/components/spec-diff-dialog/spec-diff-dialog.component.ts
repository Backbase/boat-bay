import { Component, Inject, Input } from '@angular/core';

import { Observable } from 'rxjs';
import { ReleaseSpec } from "../../pages/diff-dashboard/diff-dashboard.component";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { BoatProduct } from "../../models";

export interface Range {
  start: number;
  end: number;
}

@Component({
  selector: 'spp-spec-diff-dialog',
  templateUrl: 'spec-diff-dialog.component.html',
  styleUrls: ['spec-diff-dialog.component.scss'],
})
export class SpecDiffDialogComponent {

  releaseSpec: ReleaseSpec;
  product: BoatProduct;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.product = data.product;
    this.releaseSpec = data.releaseSpec;
  }

}
