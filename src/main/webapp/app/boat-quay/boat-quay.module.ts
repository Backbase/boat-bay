import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AceEditorComponent } from '../ace-editor/ace-editor.component';
import { RouterModule } from '@angular/router';
import { BoatQuayReportComponent } from '../boat-quay-report/boat-quay-report.component';
import { boatQuayRoutes } from './boat-quay-report.route';
import { BoatQuayDashboardComponent } from '../boat-quay-dashboard/boat-quay-dashboard.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  declarations: [AceEditorComponent, BoatQuayReportComponent, BoatQuayDashboardComponent],
  imports: [CommonModule, RouterModule.forChild(boatQuayRoutes), MatButtonModule, MatIconModule, MatCardModule],
})
export class BoatQuayModule {}
