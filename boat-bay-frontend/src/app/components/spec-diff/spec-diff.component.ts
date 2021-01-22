import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { ReleaseSpec } from "../../pages/diff-dashboard/diff-dashboard.component";

@Component({
  selector: 'app-spec-diff',
  templateUrl: 'spec-diff.component.html',
  styleUrls: ['spec-diff.component.scss'],
})
export class SpecDiffComponent implements AfterViewInit {

  @Input() specRelease! :ReleaseSpec;

  @ViewChild('acediff') editor!: ElementRef<HTMLElement>;

  ngAfterViewInit(): void {

    console.log(this.specRelease);
  }
}
