jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISpecType, SpecType } from '../spec-type.model';
import { SpecTypeService } from '../service/spec-type.service';

import { SpecTypeRoutingResolveService } from './spec-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('SpecType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SpecTypeRoutingResolveService;
    let service: SpecTypeService;
    let resultSpecType: ISpecType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SpecTypeRoutingResolveService);
      service = TestBed.inject(SpecTypeService);
      resultSpecType = undefined;
    });

    describe('resolve', () => {
      it('should return ISpecType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpecType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpecType).toEqual({ id: 123 });
      });

      it('should return new ISpecType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpecType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSpecType).toEqual(new SpecType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SpecType })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpecType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpecType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
