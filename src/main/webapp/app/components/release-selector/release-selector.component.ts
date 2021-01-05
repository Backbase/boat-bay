import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-release-selector',
  templateUrl: './release-selector.component.html',
  styleUrls: ['./release-selector.component.scss'],
})
export class ReleaseSelectorComponent {
  @Input() releaseVersionOptions: string[] | null = [];
  @Input() currentReleaseVersion: string | null = '';

  @Output() updatedReleaseVersion = new EventEmitter<string>();

  public onUpdateReleaseVersion({ value }: { value: string }) {
    this.updatedReleaseVersion.emit(value);
  }
}
