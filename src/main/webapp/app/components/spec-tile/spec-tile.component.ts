import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'app/environments/environment';
import { UiApiModule } from 'app/models';
import { ApiSpecsService } from 'app/services/api-specs.service';

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
}
