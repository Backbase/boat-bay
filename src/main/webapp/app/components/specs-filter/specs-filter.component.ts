import { Component, ElementRef, ViewChild } from '@angular/core';
import { ApiSpecsService } from 'app/services/api-specs.service';

@Component({
  selector: 'app-specs-filter',
  templateUrl: './specs-filter.component.html',
  styleUrls: ['./specs-filter.component.scss'],
})
export class SpecsFilterComponent {
  @ViewChild('searchInputEl') searchInputEl!: ElementRef;
  toShowHint = false;

  constructor(private apiSpecsService: ApiSpecsService) {}

  search(event: Event): void {
    const value = (event.target as HTMLInputElement).value;

    if (value.length === 0) {
      this.toShowHint = false;
      this.clearInput();
    } else if (value.length < 3) {
      this.toShowHint = true;
    } else {
      this.toShowHint = false;
      this.apiSpecsService.setSearchQuery(value);
    }
  }

  clearInput(): void {
    this.toShowHint = false;
    this.searchInputEl.nativeElement.value = '';
    this.apiSpecsService.setSearchQuery('');
  }
}
