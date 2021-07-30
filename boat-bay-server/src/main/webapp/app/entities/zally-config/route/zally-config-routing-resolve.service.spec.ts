jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IZallyConfig, ZallyConfig } from '../zally-config.model';
import { ZallyConfigService } from '../service/zally-config.service';

import { ZallyConfigRoutingResolveService } from './zally-config-routing-resolve.service';

describe('Service Tests', () => {
  describe('ZallyConfig routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ZallyConfigRoutingResolveService;
    let service: ZallyConfigService;
    let resultZallyConfig: IZallyConfig | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ZallyConfigRoutingResolveService);
      service = TestBed.inject(ZallyConfigService);
      resultZallyConfig = undefined;
    });

    describe('resolve', () => {
      it('should return IZallyConfig returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZallyConfig = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultZallyConfig).toEqual({ id: 123 });
      });

      it('should return new IZallyConfig if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZallyConfig = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultZallyConfig).toEqual(new ZallyConfig());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ZallyConfig })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultZallyConfig = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultZallyConfig).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
