import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductRelease, ProductRelease } from '../product-release.model';
import { ProductReleaseService } from '../service/product-release.service';

@Injectable({ providedIn: 'root' })
export class ProductReleaseRoutingResolveService implements Resolve<IProductRelease> {
  constructor(protected service: ProductReleaseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductRelease> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productRelease: HttpResponse<ProductRelease>) => {
          if (productRelease.body) {
            return of(productRelease.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductRelease());
  }
}
