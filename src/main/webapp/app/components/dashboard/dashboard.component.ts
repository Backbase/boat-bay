import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from 'app/models';
import { ApiSpecsService } from 'app/service/api-specs.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  public readonly products$: Observable<Product[]> = this.apiService.products$;
  public readonly releaseVersions$: Observable<string[]> = this.apiService.availableReleaseVersions$;
  public readonly currentReleaseVersion$: Observable<string> = this.apiService.currentReleaseVersion$;

  constructor(private readonly apiService: ApiSpecsService) {}

  public onReleaseVersionChange(updatedReleaseVersion: string): void {
    this.apiService.selectCurrentReleaseVersion(updatedReleaseVersion);
  }
}
