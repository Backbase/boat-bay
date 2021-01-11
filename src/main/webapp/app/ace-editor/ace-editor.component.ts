import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import * as ace from 'ace-builds';
import { Observable } from 'rxjs';

export interface Range {
  start: number;
  end: number;
}

@Component({
  selector: 'jhi-ace-editor',
  templateUrl: './ace-editor.component.html',
  styleUrls: ['./ace-editor.component.scss'],
})
export class AceEditorComponent implements AfterViewInit {
  @Input() contents!: string | '';
  @Input() highlight!: Observable<Range> | null;

  marker: number | undefined;

  @ViewChild('editor') editor!: ElementRef<HTMLElement>;

  ngAfterViewInit(): void {
    ace.config.set('fontSize', '14px');

    const aceEditor = ace.edit(this.editor.nativeElement);
    aceEditor.setReadOnly(true);
    aceEditor.setShowPrintMargin(false);
    aceEditor.getSession().setMode('ace/mode/yaml');
    aceEditor.session.setValue(this.contents);
    aceEditor.setAnimatedScroll(true);
    aceEditor.setScrollSpeed(1);
    const AceRange = ace.require('ace/range').Range;
    this.highlight?.subscribe(range => {
      // Clear previous marker
      if (this.marker != null) {
        aceEditor.getSession().removeMarker(this.marker);
      }
      this.marker = aceEditor.getSession().addMarker(new AceRange(range.start - 1, 0, range.end, 0), 'ace_active-line', 'fullLine');
      // Set new marker
      aceEditor.scrollToLine(range.start, true, true, (): void => {});
    });
  }
}
