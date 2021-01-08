import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import * as ace from 'ace-builds';
import { UiApiModule } from '../models/dashboard/v1';

@Component({
  selector: 'jhi-ace-editor',
  templateUrl: './ace-editor.component.html',
  styleUrls: ['./ace-editor.component.scss'],
})
export class AceEditorComponent implements AfterViewInit {
  @Input() contents!: string | '<h1>w00t</h1>';

  constructor(@ViewChild('editor') private editor: ElementRef<HTMLElement>) {}

  ngAfterViewInit(): void {
    ace.config.set('fontSize', '14px');
    const aceEditor = ace.edit(this.editor.nativeElement);
    aceEditor.session.setValue(this.contents);
  }
}
