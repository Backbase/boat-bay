import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BoatBaySharedModule } from 'app/shared/shared.module';
import { DashboardComponent } from './dashboard.component';
import { DashboardDetailComponent } from './dashboard-detail.component';
import { DashboardUpdateComponent } from './dashboard-update.component';
import { DashboardDeleteDialogComponent } from './dashboard-delete-dialog.component';
import { dashboardRoute } from './dashboard.route';

@NgModule({
  imports: [BoatBaySharedModule, RouterModule.forChild(dashboardRoute)],
  declarations: [DashboardComponent, DashboardDetailComponent, DashboardUpdateComponent, DashboardDeleteDialogComponent],
  entryComponents: [DashboardDeleteDialogComponent],
})
export class BoatBayDashboardModule {}
