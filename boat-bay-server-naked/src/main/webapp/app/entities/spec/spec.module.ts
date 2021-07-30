import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpecComponent } from './list/spec.component';
import { SpecDetailComponent } from './detail/spec-detail.component';
import { SpecUpdateComponent } from './update/spec-update.component';
import { SpecDeleteDialogComponent } from './delete/spec-delete-dialog.component';
import { SpecRoutingModule } from './route/spec-routing.module';

@NgModule({
  imports: [SharedModule, SpecRoutingModule],
  declarations: [SpecComponent, SpecDetailComponent, SpecUpdateComponent, SpecDeleteDialogComponent],
  entryComponents: [SpecDeleteDialogComponent],
})
export class SpecModule {}
