import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { BoatDashboardService } from "../services/boat-dashboard.service";
import { BoatProduct } from "../models";
import { flatMap } from "rxjs/operators";
import { HttpResponse } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BoatProductResolver implements Resolve<BoatProduct> {
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
