import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PortalDashboardComponent } from "./pages/portal-dashboard/portal-dashboard.component";
import { ProductDashboardComponent } from "./pages/product-dashboard/product-dashboard.component";
import { LintReportComponent } from "./pages/lint-report/lint-report.component";
import { BoatProductResolver } from "./resolvers/boat-product.resolver";
import { LintReportResolver } from "./resolvers/lint-report.resolver";
import { SpecTagCloudComponent } from "./components/tag-cloud/spec-tag-cloud.component";
import { TagCloudDashboardComponent } from "./pages/tag-dashboard/tag-cloud-dashboard.component";


const routes: Routes = [
  {
    path: '',
    component: PortalDashboardComponent
  },
  {
    path: ':portalKey/:productKey/lint-reports',
    component: ProductDashboardComponent,
    data: {
      pageTitle: 'Boat Bay',
    },
    resolve: {
      product: BoatProductResolver,
    }
  },
  {
    path: ':portalKey/:productKey/lint-reports/:specId',
    component: LintReportComponent,
    data: {
      pageTitle: 'Boat Bay Lint Report',
    },
    resolve: {
      product: BoatProductResolver,
      lintReport: LintReportResolver
    }
  },
  {
    path: ':portalKey/:productKey/tag-cloud',
    component: TagCloudDashboardComponent,
    data: {
      pageTitle: 'Boat Bay Lint Report',
    },
    resolve: {
      product: BoatProductResolver,
    }
  },
  {
    path: ':portalKey/:productKey/diff-reports',
    component: ProductDashboardComponent,
    data: {
      pageTitle: 'Boat Bay',
    },
    resolve: {
      product: BoatProductResolver,
    }
  },
  {
    path: '**',
    redirectTo: ''
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
