jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProductRelease, ProductRelease } from '../product-release.model';
import { ProductReleaseService } from '../service/product-release.service';

import { ProductReleaseRoutingResolveService } from './product-release-routing-resolve.service';

describe('Service Tests', () => {
  describe('ProductRelease routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProductReleaseRoutingResolveService;
    let service: ProductReleaseService;
    let resultProductRelease: IProductRelease | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProductReleaseRoutingResolveService);
      service = TestBed.inject(ProductReleaseService);
      resultProductRelease = undefined;
    });

    describe('resolve', () => {
      it('should return IProductRelease returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductRelease = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProductRelease).toEqual({ id: 123 });
      });

      it('should return new IProductRelease if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductRelease = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProductRelease).toEqual(new ProductRelease());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProductRelease })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProductRelease = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProductRelease).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
