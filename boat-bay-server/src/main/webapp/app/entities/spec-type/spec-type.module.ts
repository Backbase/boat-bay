import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { SpecTypeComponent } from './spec-type.component';
import { SpecTypeDetailComponent } from './spec-type-detail.component';
import { SpecTypeUpdateComponent } from './spec-type-update.component';
import { SpecTypeDeleteDialogComponent } from './spec-type-delete-dialog.component';
import { specTypeRoute } from './spec-type.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(specTypeRoute)],
  declarations: [SpecTypeComponent, SpecTypeDetailComponent, SpecTypeUpdateComponent, SpecTypeDeleteDialogComponent],
  entryComponents: [SpecTypeDeleteDialogComponent],
})
export class BoatBaySpecTypeModule {}
