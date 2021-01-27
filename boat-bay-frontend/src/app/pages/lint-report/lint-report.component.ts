import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Range } from '../../components/ace-editor/ace-editor.component';
import { BoatLintReport, BoatLintRule, BoatProduct, BoatViolation } from "../../models/";
import { ActivatedRoute } from "@angular/router";
import { Ace } from "ace-builds";
import { MatDialog } from "@angular/material/dialog";
import { DisableRuleModalDialogComponent } from "../../components/disable-rule-modal-dialog/disable-rule-modal-dialog.component";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import Annotation = Ace.Annotation;

@Component({
  selector: 'lint-report',
  templateUrl: 'lint-report.component.html',
  styleUrls: ['lint-report.component.scss'],
})
export class LintReportComponent implements OnInit {
  lintReport$: Observable<BoatLintReport> | null;
  product$: Observable<BoatProduct>;
  @Output() highlight = new EventEmitter<Range>();
  @Output() annotations = new EventEmitter<Annotation[]>();


  constructor(protected activatedRoute: ActivatedRoute,
              public dialog: MatDialog,
              protected boatLintReportService: BoatDashboardService,
              private _snackBar: MatSnackBar) {
    this.lintReport$ = activatedRoute.data.pipe(map(({lintReport}) => lintReport));
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));

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
    const range: Range = {
      start: violation.lines.start,
      end: violation.lines.end,
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
        this.boatLintReportService.getLintReport(product.portalKey, product.key, lintReport.spec.id, true).pipe(map(({body}) => body))
          .subscribe(updatedReport => {
            window.location.reload();
          });
      }

    });
  }
}
