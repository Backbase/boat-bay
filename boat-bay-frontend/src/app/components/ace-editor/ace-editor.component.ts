import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { Ace, Range, config, edit, require } from "ace-builds";
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

    config.set('fontSize', '14px');
    // This should be fixed by resolving the required packages from node_modules...
    config.set('basePath', 'https://unpkg.com/ace-builds@1.4.12/src-noconflict');

    const aceEditor = edit(this.editor.nativeElement);
    aceEditor.setReadOnly(true);
    aceEditor.setShowPrintMargin(false);

    aceEditor.getSession().setMode('ace/mode/yaml');

    if(this.contents) {
      aceEditor.session.setValue(this.contents);
    }
    aceEditor.setAnimatedScroll(true);
    aceEditor.setScrollSpeed(1);
    aceEditor.setTheme("ace/theme/chrome");
    const AceRange = require('ace/range').Range;
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
