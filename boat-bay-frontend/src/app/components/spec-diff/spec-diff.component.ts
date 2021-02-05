import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { ReleaseSpec } from "../../pages/diff-dashboard/diff-dashboard.component";
import { Ace, Range, config, edit, require } from "ace-builds";

import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { Observable, zip } from "rxjs";
import { BoatProduct, BoatSpec } from "../../models";
import { map } from "rxjs/operators";
import * as AceDiff from "ace-diff";


@Component({
  selector: 'app-spec-diff',
  templateUrl: 'spec-diff.component.html',
  styleUrls: ['spec-diff.component.scss'],
})
export class SpecDiffComponent implements AfterViewInit {

  @Input() product!: BoatProduct;
  @Input() specRelease!: ReleaseSpec;

  @ViewChild('aceDiff') editor!: ElementRef<HTMLElement>;
  private differ!: AceDiff;
  diffIndex = 0;


  constructor(public dashboard: BoatDashboardService) {
  }


  ngAfterViewInit(): void {

    if (this.specRelease && this.specRelease.spec1 && this.specRelease.spec2 && this.product) {

      config.set('fontSize', '14px');
      // This should be fixed by resolving the required packages from node_modules...
      config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');

      const spec1$: Observable<BoatSpec> = this.dashboard.getSpecBySpec(this.product, this.specRelease.spec1).pipe(
        map(({body}) => this.mapBody(body)));

      const spec2$ = this.dashboard.getSpecBySpec(this.product, this.specRelease.spec2).pipe(
        map(({body}) => this.mapBody(body)));

      zip(spec1$, spec2$).subscribe(specs => {
        const spec1 = specs[0];
        const spec2 = specs[1];

        this.differ = new AceDiff({
          element: this.editor.nativeElement,
          mode: 'ace/mode/yaml',
          left: {
            content: spec1.openApi,
            editable: false
          },
          right: {
            content: spec2.openApi,
            editable: false
          }
        });

      })

    }

  }

  public scrollToDiff(delta: number): void {
    if (this.differ) {
      const differ = this.differ;

      // @ts-ignore
      let diffs = differ["diffs"]

      this.diffIndex += delta;

      if (this.diffIndex < 0) {
        this.diffIndex = diffs.length - 1;
      } else if (this.diffIndex > diffs.length - 1) {
        this.diffIndex = 0;
      }

      var lrow = diffs[this.diffIndex].leftStartLine;
      var rrow = diffs[this.diffIndex].rightStartLine;

      if (lrow > 5) {
        lrow -= 5;
      }

      if (rrow > 5) {
        rrow -= 5;
      }

      differ.getEditors().left.scrollToLine(lrow);
      differ.getEditors().right.scrollToLine(rrow);
    }
  }


  private mapBody(body: BoatSpec | null) {
    if (body) {
      return body;
    }
    throw new TypeError('spec cannot be null')
  }

  next() {
    this.scrollToDiff(1);
  }

  previous() {
    this.scrollToDiff(-1);
  }
}
