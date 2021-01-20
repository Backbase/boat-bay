import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { BoatLintReport, BoatLintRule, BoatProduct } from "../../models";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { MatSnackBar } from "@angular/material/snack-bar";

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
              protected dashboardService: BoatDashboardService,
              private _snackBar: MatSnackBar
  ) {

    this.rule = data.rule;
    this.product = data.product;
    this.lintReport = data.lintReport;
  }

  ngOnInit(): void {
  }

  disableRule():void {
    this.rule.enabled = false;
    this.dashboardService.postPortalLintRule(this.product.portalKey, this.rule)
      .subscribe(result => {
      if (result.ok) {
        this.dialogRef.close({event: 'Ok'});
      } else {
        this._snackBar.open("Error disabling rule...Please contact Chuck Norris");
      }
    })
  }

  closeDialog():void {
    this.dialogRef.close({event: 'Cancel'});
  }

}
