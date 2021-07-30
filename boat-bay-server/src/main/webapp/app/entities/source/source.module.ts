import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SourceComponent } from './list/source.component';
import { SourceDetailComponent } from './detail/source-detail.component';
import { SourceUpdateComponent } from './update/source-update.component';
import { SourceDeleteDialogComponent } from './delete/source-delete-dialog.component';
import { SourceRoutingModule } from './route/source-routing.module';

@NgModule({
  imports: [SharedModule, SourceRoutingModule],
  declarations: [SourceComponent, SourceDetailComponent, SourceUpdateComponent, SourceDeleteDialogComponent],
  entryComponents: [SourceDeleteDialogComponent],
})
export class SourceModule {}
