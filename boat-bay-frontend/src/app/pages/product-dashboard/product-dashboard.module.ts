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
import { ProductDashboardComponent } from "./product-dashboard.component";
import { CapabilityTableComponent } from "../../components/capability-table/capability-table.component";
import { ServiceDefinitionTableComponent } from "../../components/service-definition-table/service-definition-table.component";
import { PortalDashboardModule } from "../portal-dashboard/portal-dashboard.module";
import { SpecsTableComponent } from "../../components/specs-table/specs-table.component";
import { SpecFilterComponent } from "../../components/spec-filter/spec-filter.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatListModule } from "@angular/material/list";


@NgModule({
  declarations: [
    ProductDashboardComponent,
    CapabilityTableComponent,
    ServiceDefinitionTableComponent,
    SpecsTableComponent,
    SpecFilterComponent,
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
  ]
})
export class ProductDashboardModule { }
