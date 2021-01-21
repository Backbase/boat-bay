import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import * as ace from 'ace-builds';

import { Observable } from 'rxjs';
import { Ace } from "ace-builds";
import Annotation = Ace.Annotation;

export interface Range {
  start: number;
  end: number;
}

@Component({
  selector: 'ace-editor',
  templateUrl: 'ace-editor.component.html',
  styleUrls: ['ace-editor.component.scss'],
})
export class AceEditorComponent implements AfterViewInit {
  @Input() contents!: string | undefined;

  @Input() highlight: Observable<Range> | null = null;
  @Input() annotations: Observable<Annotation[]> | null = null;

  marker: number | undefined;

  @ViewChild('editor') editor!: ElementRef<HTMLElement>;

  ngAfterViewInit(): void {
    ace.config.set('fontSize', '14px');
    // This should be fixed by resolving the required packages from node_modules...
    ace.config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');


    const aceEditor = ace.edit(this.editor.nativeElement);
    aceEditor.setReadOnly(true);
    aceEditor.setShowPrintMargin(false);

    aceEditor.getSession().setMode('ace/mode/yaml');

    if(this.contents) {
      aceEditor.session.setValue(this.contents);
    }
    aceEditor.setAnimatedScroll(true);
    aceEditor.setScrollSpeed(1);
    aceEditor.setTheme("ace/theme/chrome");
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


    // guttor of lines containing violation should be colored the violation....
    this.annotations?.subscribe(annotations => {
      aceEditor.getSession().setAnnotations(annotations);
    })
  }
}
