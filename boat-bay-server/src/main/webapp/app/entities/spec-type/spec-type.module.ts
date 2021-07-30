import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpecTypeComponent } from './list/spec-type.component';
import { SpecTypeDetailComponent } from './detail/spec-type-detail.component';
import { SpecTypeUpdateComponent } from './update/spec-type-update.component';
import { SpecTypeDeleteDialogComponent } from './delete/spec-type-delete-dialog.component';
import { SpecTypeRoutingModule } from './route/spec-type-routing.module';

@NgModule({
  imports: [SharedModule, SpecTypeRoutingModule],
  declarations: [SpecTypeComponent, SpecTypeDetailComponent, SpecTypeUpdateComponent, SpecTypeDeleteDialogComponent],
  entryComponents: [SpecTypeDeleteDialogComponent],
})
export class SpecTypeModule {}
