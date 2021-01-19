import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { ProductReleaseComponent } from './product-release.component';
import { ProductReleaseDetailComponent } from './product-release-detail.component';
import { ProductReleaseUpdateComponent } from './product-release-update.component';
import { ProductReleaseDeleteDialogComponent } from './product-release-delete-dialog.component';
import { productReleaseRoute } from './product-release.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(productReleaseRoute)],
  declarations: [
    ProductReleaseComponent,
    ProductReleaseDetailComponent,
    ProductReleaseUpdateComponent,
    ProductReleaseDeleteDialogComponent,
  ],
  entryComponents: [ProductReleaseDeleteDialogComponent],
})
export class BoatBayProductReleaseModule {}
