import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LintReport, Violation } from 'app/models/lint-report';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Range } from 'app/ace-editor/ace-editor.component';

@Component({
  selector: 'bb-quay-report',
  templateUrl: './boat-quay-report.component.html',
  styleUrls: ['./boat-quay-report.component.scss'],
})
export class BoatQuayReportComponent implements OnInit {
  lintReport$: Observable<LintReport>;
  @Output() highlight = new EventEmitter<Range>();

  constructor(protected activatedRoute: ActivatedRoute) {
    this.lintReport$ = activatedRoute.data.pipe(map(({ lintReport }) => lintReport));
  }

  ngOnInit(): void {}

  mark(violation: Violation): void {
    const range: Range = {
      start: violation.lines.start,
      end: violation.lines.end,
    };
    this.highlight.emit(range);
  }
}
