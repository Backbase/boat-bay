import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DashboardComponent } from './list/dashboard.component';
import { DashboardDetailComponent } from './detail/dashboard-detail.component';
import { DashboardUpdateComponent } from './update/dashboard-update.component';
import { DashboardDeleteDialogComponent } from './delete/dashboard-delete-dialog.component';
import { DashboardRoutingModule } from './route/dashboard-routing.module';

@NgModule({
  imports: [SharedModule, DashboardRoutingModule],
  declarations: [DashboardComponent, DashboardDetailComponent, DashboardUpdateComponent, DashboardDeleteDialogComponent],
  entryComponents: [DashboardDeleteDialogComponent],
})
export class DashboardModule {}
