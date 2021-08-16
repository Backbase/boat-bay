import {AfterViewInit, Component, ElementRef, Input, ViewChild} from '@angular/core';
import {ReleaseSpec} from "../../pages/diff-dashboard/diff-dashboard.component";
import {config} from "ace-builds";

import {Observable, zip} from "rxjs";
import * as AceDiff from "ace-diff";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatSpec} from "../../services/dashboard/model/boatSpec";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";


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


  constructor(public dashboard: DashboardHttpService) {
  }


  ngAfterViewInit(): void {

    if (this.specRelease && this.specRelease.spec1 && this.specRelease.spec2 && this.product) {

      config.set('fontSize', '14px');
      // This should be fixed by resolving the required packages from node_modules...
      config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');

      const spec1$: Observable<BoatSpec> = this.dashboard.getSpec(
        {
          portalKey: this.product.portalKey,
          productKey: this.product.key,
          capabilityKey: this.specRelease.spec1.capability.key,
          serviceKey: this.specRelease.spec1.serviceDefinition.key,
          specKey: this.specRelease.spec1.key,
          version: this.specRelease.spec1.version
        }
      );

      const spec2$ = this.dashboard.getSpec({
        portalKey: this.product.portalKey,
        productKey: this.product.key,
        capabilityKey: this.specRelease.spec2.capability.key,
        serviceKey: this.specRelease.spec2.serviceDefinition.key,
        specKey: this.specRelease.spec2.key,
        version: this.specRelease.spec2.version
        }
      );


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
