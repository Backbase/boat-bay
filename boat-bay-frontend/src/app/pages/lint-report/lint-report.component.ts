import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { IRange } from '../../components/ace-editor/ace-editor.component';
import { ActivatedRoute } from "@angular/router";
import { Ace } from "ace-builds";
import { MatDialog } from "@angular/material/dialog";
import { DisableRuleModalDialogComponent } from "../../components/disable-rule-modal-dialog/disable-rule-modal-dialog.component";
import { MatSnackBar } from "@angular/material/snack-bar";
import Annotation = Ace.Annotation;
import {BoatLintReport} from "../../services/dashboard/model/boatLintReport";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatViolation} from "../../services/dashboard/model/boatViolation";
import {BoatLintRule} from "../../services/dashboard/model/boatLintRule";

@Component({
  selector: 'lint-report',
  templateUrl: 'lint-report.component.html',
  styleUrls: ['lint-report.component.scss'],
})
export class LintReportComponent implements OnInit {
  lintReport$: Observable<BoatLintReport>;
  product$: Observable<BoatProduct>;
  @Output() highlight = new EventEmitter<IRange>();
  @Output() annotations = new EventEmitter<Annotation[]>();


  constructor(protected activatedRoute: ActivatedRoute,
              public dialog: MatDialog,
              protected boatLintReportService: DashboardHttpService,
              private _snackBar: MatSnackBar) {
    this.lintReport$ = activatedRoute.data.pipe(map(({lintReport}) => lintReport));
    this.product$ = activatedRoute.data.pipe(map(({product}) => product), tap(product => console.log("p", product)));

    this.lintReport$.pipe(
      map(report => {
        const annotations: Annotation[] = report.violations.map(violation => {
            let annotation: Annotation = {
              text: violation.description,
              type: violation.severity,
              row: violation.lines.start
            };
            return annotation;
          },
        )
        this.annotations.emit(annotations);
      })).subscribe();
  }

  ngOnInit(): void {
  }

  mark(violation: BoatViolation): void {
    const range: IRange = {
      start: violation.lines.start,
      end: violation.lines.endInclusive,
    };
    this.highlight.emit(range);
  }

  disableRule(lintReport: BoatLintReport, boatProduct: BoatProduct, rule: BoatLintRule): void {
    const dialogRef = this.dialog.open(DisableRuleModalDialogComponent, {
      data: {
        product: boatProduct,
        lintReport: lintReport,
        rule: rule
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result.event === 'OK') {
        const lintReport: BoatLintReport = result.data.lintReport;
        const product: BoatProduct = result.data.product;
        this._snackBar.open(`Relinting spec ${lintReport.spec.title} with updated rules. Reloading when done....`);
        this.boatLintReportService.getLintReportForSpec(
          {
            portalKey: product.portalKey,
            productKey: product.key,
            specId: lintReport.spec.id,
            refresh: true
          }, "response").pipe(map(({body}) => body))
          .subscribe(updatedReport => {
            window.location.reload();
          });
      }

    });
  }
}
