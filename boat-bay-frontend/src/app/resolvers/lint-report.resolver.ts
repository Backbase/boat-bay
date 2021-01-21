import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { BoatDashboardService } from "../services/boat-dashboard.service";
import { BoatLintReport } from "../models";
import { flatMap } from "rxjs/operators";
import { HttpResponse } from "@angular/common/http";

@Injectable({providedIn: 'root'})
export class LintReportResolver implements Resolve<BoatLintReport> {
  constructor(protected boatLintReportService: BoatDashboardService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<BoatLintReport> | Promise<BoatLintReport> | BoatLintReport {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    const specId = route.params['specId']
    if (specId && portalKey && productKey) {
      return this.boatLintReportService.getLintReport(portalKey, productKey, specId).pipe(
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
