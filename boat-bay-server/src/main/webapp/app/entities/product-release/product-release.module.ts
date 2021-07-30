import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductReleaseComponent } from './list/product-release.component';
import { ProductReleaseDetailComponent } from './detail/product-release-detail.component';
import { ProductReleaseUpdateComponent } from './update/product-release-update.component';
import { ProductReleaseDeleteDialogComponent } from './delete/product-release-delete-dialog.component';
import { ProductReleaseRoutingModule } from './route/product-release-routing.module';

@NgModule({
  imports: [SharedModule, ProductReleaseRoutingModule],
  declarations: [
    ProductReleaseComponent,
    ProductReleaseDetailComponent,
    ProductReleaseUpdateComponent,
    ProductReleaseDeleteDialogComponent,
  ],
  entryComponents: [ProductReleaseDeleteDialogComponent],
})
export class ProductReleaseModule {}
