import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { TagComponent } from './tag.component';
import { TagDetailComponent } from './tag-detail.component';
import { TagUpdateComponent } from './tag-update.component';
import { TagDeleteDialogComponent } from './tag-delete-dialog.component';
import { tagRoute } from './tag.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(tagRoute)],
  declarations: [TagComponent, TagDetailComponent, TagUpdateComponent, TagDeleteDialogComponent],
  entryComponents: [TagDeleteDialogComponent],
})
export class BoatBayTagModule {}
