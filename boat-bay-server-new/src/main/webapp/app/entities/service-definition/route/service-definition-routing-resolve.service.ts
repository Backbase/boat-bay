import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceDefinition, ServiceDefinition } from '../service-definition.model';
import { ServiceDefinitionService } from '../service/service-definition.service';

@Injectable({ providedIn: 'root' })
export class ServiceDefinitionRoutingResolveService implements Resolve<IServiceDefinition> {
  constructor(protected service: ServiceDefinitionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceDefinition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceDefinition: HttpResponse<ServiceDefinition>) => {
          if (serviceDefinition.body) {
            return of(serviceDefinition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceDefinition());
  }
}
