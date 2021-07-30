jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISpec, Spec } from '../spec.model';
import { SpecService } from '../service/spec.service';

import { SpecRoutingResolveService } from './spec-routing-resolve.service';

describe('Service Tests', () => {
  describe('Spec routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SpecRoutingResolveService;
    let service: SpecService;
    let resultSpec: ISpec | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SpecRoutingResolveService);
      service = TestBed.inject(SpecService);
      resultSpec = undefined;
    });

    describe('resolve', () => {
      it('should return ISpec returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpec = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpec).toEqual({ id: 123 });
      });

      it('should return new ISpec if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpec = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSpec).toEqual(new Spec());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Spec })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpec = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpec).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
