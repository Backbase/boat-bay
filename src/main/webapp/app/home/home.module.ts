import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { ProductComponent } from 'app/components/product/product.component';
import { SpecTileComponent } from 'app/components/spec-tile/spec-tile.component';
import { ReleaseSelectorComponent } from 'app/components/release-selector/release-selector.component';
import { SpecsFilterComponent } from 'app/components/specs-filter/specs-filter.component';
import { DashboardComponent } from 'app/components/dashboard/dashboard.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';

@NgModule({
  imports: [
    BoatBaySharedModule,
    RouterModule.forChild([HOME_ROUTE]),
    MatExpansionModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatCardModule,
    MatSelectModule,
  ],
  declarations: [HomeComponent, DashboardComponent, ProductComponent, SpecTileComponent, ReleaseSelectorComponent, SpecsFilterComponent],
})
export class BoatBayHomeModule {}
