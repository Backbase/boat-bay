import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatLintReport} from "../../services/dashboard/model/boatLintReport";
import {BoatLintRule} from "../../services/dashboard/model/boatLintRule";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";

@Component({
  selector: 'app-disable-rule-modal-dialog',
  templateUrl: './disable-rule-modal-dialog.component.html',
  styleUrls: ['./disable-rule-modal-dialog.component.scss']
})
export class DisableRuleModalDialogComponent implements OnInit {

  lintReport?: BoatLintReport;
  rule: BoatLintRule;
  product: BoatProduct

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              protected dialogRef: MatDialogRef<DisableRuleModalDialogComponent>,
              protected dashboardService: DashboardHttpService,
              private _snackBar: MatSnackBar
  ) {

    this.rule = data.rule;
    this.product = data.product;
    this.lintReport = data.lintReport;
  }

  ngOnInit(): void {
  }

  disableRule(): void {
    this.rule.enabled = false;
    this.dashboardService.updatePortalLintRule (
      {
        lintRuleId: this.rule.ruleId,
        portalKey: this.product.portalKey,
        boatLintRule: this.rule
      }, "response")
      .subscribe(result => {
        if (result.ok) {
          this.dialogRef.close({
            event: 'OK',
            data: {
              lintReport: this.lintReport,
              product: this.product
            }
          });
        } else {
          this._snackBar.open("Error disabling rule...Please contact Chuck Norris");
        }
      })
  }

  closeDialog(): void {
    this.dialogRef.close({event: 'Cancel'});
  }

}
