import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterModule, Routes } from '@angular/router';
import { BoatLintReport, BoatProduct } from "./models/";
import { BoatDashboardService } from "./services/boat-dashboard.service";
import { EMPTY, Observable, of } from "rxjs";
import { HttpResponse } from "@angular/common/http";
import { flatMap } from "rxjs/operators";
import { PortalDashboardComponent } from "./pages/portal-dashboard/portal-dashboard.component";
import { ProductDashboardComponent } from "./pages/product-dashboard/product-dashboard.component";
import { LintReportComponent } from "./pages/lint-report/lint-report.component";

@Injectable({providedIn: 'root'})
export class BoatProductDashboardResolver implements Resolve<BoatProduct> {
  constructor(protected portalService: BoatDashboardService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<BoatProduct> | Promise<BoatProduct> | BoatProduct {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    if (portalKey && productKey) {
      return this.portalService.getBoatProducts(portalKey, productKey).pipe(
        flatMap((response: HttpResponse<BoatProduct>) => {
          if (response.body) {
            return of(response.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    this.router.navigate(['404']);
    return EMPTY;
  }
}

@Injectable({providedIn: 'root'})
export class LintReportResolver implements Resolve<BoatLintReport> {
  constructor(protected boatLintReportService: BoatDashboardService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<BoatLintReport> | Promise<BoatLintReport> | BoatLintReport {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    const specId = route.params['specId']
    if (specId && portalKey && productKey) {
      return this.boatLintReportService.getReport(portalKey, productKey, specId).pipe(
        flatMap((response: HttpResponse<BoatLintReport>) => {
          if (response.body) {
            return of(response.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    this.router.navigate(['404']);
    return EMPTY;
  }
}

const routes: Routes = [
  {
    path: '',
    component: PortalDashboardComponent
  },
  {
    path: ':portalKey/:productKey',
    component: ProductDashboardComponent,
    data: {
      pageTitle: 'Boat Bay',
    },
    resolve: {
      product: BoatProductDashboardResolver,
    }
  },
  {
    path: ':portalKey/:productKey/specs/:specId/lint-report',
    component: LintReportComponent,
    data: {
      pageTitle: 'Boat Bay Lint Report',
    },
    resolve: {
      product: BoatProductDashboardResolver,
      lintReport: LintReportResolver
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
