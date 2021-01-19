import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Range } from '../../components/ace-editor/ace-editor.component';
import { BoatLintReport, BoatProduct, BoatViolation } from "../../models/";
import { ActivatedRoute } from "@angular/router";
import { Ace } from "ace-builds";
import Annotation = Ace.Annotation;

@Component({
  selector: 'lint-report',
  templateUrl: 'lint-report.component.html',
  styleUrls: ['lint-report.component.scss'],
})
export class LintReportComponent implements OnInit {
  lintReport$: Observable<BoatLintReport>;
  product$: Observable<BoatProduct>;
  @Output() highlight = new EventEmitter<Range>();
  @Output() annotations = new EventEmitter<Annotation[]>();


  constructor(protected activatedRoute: ActivatedRoute) {
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

  // constructor(private httpClient: HttpClient) {
  //   this.lintReport$ = httpClient.get('/assets/mock/lint-report.json').pipe(map(value => value as BoatLintReport));
  //
  // }


  ngOnInit(): void {
  }

  mark(violation: BoatViolation): void {
    const range: Range = {
      start: violation.lines.start,
      end: violation.lines.end,
    };
    this.highlight.emit(range);
  }
}
