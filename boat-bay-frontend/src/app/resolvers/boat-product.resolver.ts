import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {flatMap} from "rxjs/operators";
import {HttpResponse} from "@angular/common/http";
import {BoatProduct} from "../services/dashboard/model/boatProduct";
import {DashboardHttpService} from "../services/dashboard/api/dashboard.service";

@Injectable({
  providedIn: 'root'
})
export class BoatProductResolver implements Resolve<BoatProduct> {
  constructor(protected portalService: DashboardHttpService, private router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<BoatProduct> | Promise<BoatProduct> | BoatProduct {
    const portalKey = route.params['portalKey'];
    const productKey = route.params['productKey'];
    if (portalKey && productKey) {
      return this.portalService.getPortalProduct({portalKey: portalKey, productKey: productKey}, "response").pipe(
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
