import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { SourcePathComponent } from './source-path.component';
import { SourcePathDetailComponent } from './source-path-detail.component';
import { SourcePathUpdateComponent } from './source-path-update.component';
import { SourcePathDeleteDialogComponent } from './source-path-delete-dialog.component';
import { sourcePathRoute } from './source-path.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(sourcePathRoute)],
  declarations: [SourcePathComponent, SourcePathDetailComponent, SourcePathUpdateComponent, SourcePathDeleteDialogComponent],
  entryComponents: [SourcePathDeleteDialogComponent],
})
export class BoatBaySourcePathModule {}
