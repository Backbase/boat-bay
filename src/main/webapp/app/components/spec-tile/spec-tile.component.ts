import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'app/environments/environment';
import { UiApiModule } from 'app/models/dashboard/v1';
import { ApiSpecsService } from 'app/components/api-specs.service';

@Component({
  selector: 'app-spec-tile',
  templateUrl: './spec-tile.component.html',
  styleUrls: ['./spec-tile.component.scss'],
})
export class SpecTileComponent implements OnInit {
  @Input() service!: UiApiModule;
  public tileColor!: string;
  readonly searchableString = this.apiSpecsService.searchQuery$;
  public titleLength = environment.lengthOfTitle;
  public descriptionLength = environment.lengthOfDescription;

  constructor(private apiSpecsService: ApiSpecsService) {}

  ngOnInit(): void {
    this.tileColor = this.apiSpecsService.getSpecColorAsPerTags(this.service.tags);
  }

  hideTag(service: UiApiModule, tag: string): void {
    this.apiSpecsService.hideTag(tag).subscribe(response => {
      if (response.ok) {
        const index: number = service.tags.indexOf(tag);
        if (index !== -1) {
          service.tags.splice(index, 1);
        }
      }
    });
  }
}
