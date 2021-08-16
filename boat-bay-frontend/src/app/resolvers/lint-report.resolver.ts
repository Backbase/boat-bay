import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {flatMap} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";
import {DashboardHttpService} from "../services/dashboard/api/dashboard.service";
import {BoatLintReport} from "../services/dashboard/model/boatLintReport";

@Injectable({providedIn: 'root'})
export class LintReportResolver implements Resolve<BoatLintReport> {
  constructor(protected boatLintReportService: DashboardHttpService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<BoatLintReport> | Promise<BoatLintReport> | BoatLintReport {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    const specId = route.params['specId']
    if (specId && portalKey && productKey) {
      return this.boatLintReportService.getLintReportForSpec({
        portalKey: portalKey,
        productKey: productKey,
        specId: specId
      }, "response").pipe(
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
