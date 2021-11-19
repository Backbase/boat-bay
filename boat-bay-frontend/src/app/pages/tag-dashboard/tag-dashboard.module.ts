import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from "@angular/router";
import { TagCloudDashboardComponent } from "./tag-cloud-dashboard.component";
import { TagCloudModule } from 'angular-tag-cloud-module';
import { SpecTagCloudComponent } from "../../components/tag-cloud/spec-tag-cloud.component";
import { BoatProductResolver } from "../../resolvers/boat-product.resolver";

const routes: Routes = [
  {
    path: ':portalKey/:productKey',
    component: TagCloudDashboardComponent,
    data: {
      pageTitle: 'Boat Bay Tag Cloud',
    },
    resolve: {
      product: BoatProductResolver,
    }
  }
];

@NgModule({
  declarations: [
    SpecTagCloudComponent,
    TagCloudDashboardComponent,
  ],
  imports: [
    RouterModule.forChild(routes),
    TagCloudModule,
    CommonModule
  ]
})
export class TagDashboardModule {
}
