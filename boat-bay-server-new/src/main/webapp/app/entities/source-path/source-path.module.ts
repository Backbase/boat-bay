import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SourcePathComponent } from './list/source-path.component';
import { SourcePathDetailComponent } from './detail/source-path-detail.component';
import { SourcePathUpdateComponent } from './update/source-path-update.component';
import { SourcePathDeleteDialogComponent } from './delete/source-path-delete-dialog.component';
import { SourcePathRoutingModule } from './route/source-path-routing.module';

@NgModule({
  imports: [SharedModule, SourcePathRoutingModule],
  declarations: [SourcePathComponent, SourcePathDetailComponent, SourcePathUpdateComponent, SourcePathDeleteDialogComponent],
  entryComponents: [SourcePathDeleteDialogComponent],
})
export class SourcePathModule {}
