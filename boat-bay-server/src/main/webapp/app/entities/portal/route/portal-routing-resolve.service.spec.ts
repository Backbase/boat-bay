jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPortal, Portal } from '../portal.model';
import { PortalService } from '../service/portal.service';

import { PortalRoutingResolveService } from './portal-routing-resolve.service';

describe('Service Tests', () => {
  describe('Portal routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PortalRoutingResolveService;
    let service: PortalService;
    let resultPortal: IPortal | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PortalRoutingResolveService);
      service = TestBed.inject(PortalService);
      resultPortal = undefined;
    });

    describe('resolve', () => {
      it('should return IPortal returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPortal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPortal).toEqual({ id: 123 });
      });

      it('should return new IPortal if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPortal = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPortal).toEqual(new Portal());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Portal })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPortal = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPortal).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
