jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISourcePath, SourcePath } from '../source-path.model';
import { SourcePathService } from '../service/source-path.service';

import { SourcePathRoutingResolveService } from './source-path-routing-resolve.service';

describe('Service Tests', () => {
  describe('SourcePath routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SourcePathRoutingResolveService;
    let service: SourcePathService;
    let resultSourcePath: ISourcePath | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SourcePathRoutingResolveService);
      service = TestBed.inject(SourcePathService);
      resultSourcePath = undefined;
    });

    describe('resolve', () => {
      it('should return ISourcePath returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSourcePath = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSourcePath).toEqual({ id: 123 });
      });

      it('should return new ISourcePath if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSourcePath = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSourcePath).toEqual(new SourcePath());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SourcePath })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSourcePath = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSourcePath).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
