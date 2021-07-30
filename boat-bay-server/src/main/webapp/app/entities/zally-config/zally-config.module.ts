import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ZallyConfigComponent } from './list/zally-config.component';
import { ZallyConfigDetailComponent } from './detail/zally-config-detail.component';
import { ZallyConfigUpdateComponent } from './update/zally-config-update.component';
import { ZallyConfigDeleteDialogComponent } from './delete/zally-config-delete-dialog.component';
import { ZallyConfigRoutingModule } from './route/zally-config-routing.module';

@NgModule({
  imports: [SharedModule, ZallyConfigRoutingModule],
  declarations: [ZallyConfigComponent, ZallyConfigDetailComponent, ZallyConfigUpdateComponent, ZallyConfigDeleteDialogComponent],
  entryComponents: [ZallyConfigDeleteDialogComponent],
})
export class ZallyConfigModule {}
