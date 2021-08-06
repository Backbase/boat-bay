import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from "@angular/material/card";
import { RouterModule, Routes } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatTableModule } from "@angular/material/table";
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { ListReportsComponent } from "./list-reports.component";
import { SpecsTableComponent } from "../../components/specs-table/specs-table.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatListModule } from "@angular/material/list";
import { BoatProductResolver } from "../../resolvers/boat-product.resolver";
import { LintReportComponent } from "../lint-report/lint-report.component";
import { LintReportResolver } from "../../resolvers/lint-report.resolver";
import { MatSnackBarModule } from "@angular/material/snack-bar";

const routes: Routes = [
  {
    path: ':portalKey/:productKey',
    component: ListReportsComponent,
    data: {
      pageTitle: 'Boat Bay Lint Report',
    },
    resolve: {
      product: BoatProductResolver,
    }
  },
  {
    path: ':portalKey/:productKey/lint-report/:specId',
    component: LintReportComponent,
    data: {
      pageTitle: 'Boat Bay Lint Report',
    },
    resolve: {
      product: BoatProductResolver,
      lintReport: LintReportResolver
    }
  },

];


@NgModule({
  declarations: [
    ListReportsComponent,
    SpecsTableComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    MatCardModule,
    RouterModule,
    MatButtonModule,
    MatExpansionModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatListModule,
    FormsModule,
    MatSnackBarModule
  ]
})
export class LintReportsModule { }
