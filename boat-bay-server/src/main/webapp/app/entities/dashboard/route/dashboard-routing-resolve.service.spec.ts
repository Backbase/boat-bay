jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDashboard, Dashboard } from '../dashboard.model';
import { DashboardService } from '../service/dashboard.service';

import { DashboardRoutingResolveService } from './dashboard-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dashboard routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DashboardRoutingResolveService;
    let service: DashboardService;
    let resultDashboard: IDashboard | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DashboardRoutingResolveService);
      service = TestBed.inject(DashboardService);
      resultDashboard = undefined;
    });

    describe('resolve', () => {
      it('should return IDashboard returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDashboard = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDashboard).toEqual({ id: 123 });
      });

      it('should return new IDashboard if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDashboard = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDashboard).toEqual(new Dashboard());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Dashboard })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDashboard = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDashboard).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
