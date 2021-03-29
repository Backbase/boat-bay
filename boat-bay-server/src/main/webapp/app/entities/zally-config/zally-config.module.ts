import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { ZallyConfigComponent } from './zally-config.component';
import { ZallyConfigDetailComponent } from './zally-config-detail.component';
import { ZallyConfigUpdateComponent } from './zally-config-update.component';
import { ZallyConfigDeleteDialogComponent } from './zally-config-delete-dialog.component';
import { zallyConfigRoute } from './zally-config.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(zallyConfigRoute)],
  declarations: [ZallyConfigComponent, ZallyConfigDetailComponent, ZallyConfigUpdateComponent, ZallyConfigDeleteDialogComponent],
  entryComponents: [ZallyConfigDeleteDialogComponent],
})
export class BoatBayZallyConfigModule {}
