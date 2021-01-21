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
import { TagCloudDashboardComponent } from "./tag-cloud-dashboard.component";
import { PortalDashboardModule } from "../portal-dashboard/portal-dashboard.module";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatListModule } from "@angular/material/list";
import { TagCloudModule } from 'angular-tag-cloud-module';
import { SpecTagCloudComponent } from "../../components/tag-cloud/spec-tag-cloud.component";


@NgModule({
  declarations: [
    SpecTagCloudComponent,
    TagCloudDashboardComponent,
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
    PortalDashboardModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatListModule,
    FormsModule,
    TagCloudModule

  ]
})
export class TagDashboardModule {
}
