import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'app/environments/environment';
import { UiApiModule } from 'app/models';
import { ApiSpecsService } from 'app/service/api-specs.service';

@Component({
  selector: 'app-spec-tile',
  templateUrl: './spec-tile.component.html',
  styleUrls: ['./spec-tile.component.scss'],
})
export class SpecTileComponent implements OnInit {
  @Input() spec!: UiApiModule;
  public tileColor!: string;
  readonly searchableString = this.apiSpecsService.searchQuery$;
  public titleLength = environment.lengthOfTitle!;
  public descriptionLength = environment.lengthOfDescription!;

  constructor(private readonly apiSpecsService: ApiSpecsService) {}

  ngOnInit(): void {
    this.tileColor = this.apiSpecsService.getSpecColorAsPerTags(this.spec.tags);
  }
}
