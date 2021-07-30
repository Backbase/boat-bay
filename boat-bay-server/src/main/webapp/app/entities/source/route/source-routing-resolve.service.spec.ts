jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISource, Source } from '../source.model';
import { SourceService } from '../service/source.service';

import { SourceRoutingResolveService } from './source-routing-resolve.service';

describe('Service Tests', () => {
  describe('Source routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SourceRoutingResolveService;
    let service: SourceService;
    let resultSource: ISource | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SourceRoutingResolveService);
      service = TestBed.inject(SourceService);
      resultSource = undefined;
    });

    describe('resolve', () => {
      it('should return ISource returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSource).toEqual({ id: 123 });
      });

      it('should return new ISource if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSource = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSource).toEqual(new Source());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Source })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSource).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
