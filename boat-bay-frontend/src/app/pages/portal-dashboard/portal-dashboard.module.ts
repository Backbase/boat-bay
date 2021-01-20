import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from "@angular/material/tabs";
import { MatCardModule } from "@angular/material/card";
import { RouterModule } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { CurrentViolationsComponent } from "../../components/current-violations/current-violations.component";
import { PortalDashboardComponent } from "./portal-dashboard.component";
import { MatIconModule } from "@angular/material/icon";


@NgModule({
  declarations: [
    PortalDashboardComponent,
    CurrentViolationsComponent
  ],
  exports: [
    CurrentViolationsComponent
  ],
  imports: [
    CommonModule,
    MatTabsModule,
    MatCardModule,
    RouterModule,
    MatButtonModule,
    MatExpansionModule,
    MatGridListModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatIconModule,
  ]
})
export class PortalDashboardModule { }
