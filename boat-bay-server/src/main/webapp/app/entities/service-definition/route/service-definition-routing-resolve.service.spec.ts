jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IServiceDefinition, ServiceDefinition } from '../service-definition.model';
import { ServiceDefinitionService } from '../service/service-definition.service';

import { ServiceDefinitionRoutingResolveService } from './service-definition-routing-resolve.service';

describe('Service Tests', () => {
  describe('ServiceDefinition routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ServiceDefinitionRoutingResolveService;
    let service: ServiceDefinitionService;
    let resultServiceDefinition: IServiceDefinition | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ServiceDefinitionRoutingResolveService);
      service = TestBed.inject(ServiceDefinitionService);
      resultServiceDefinition = undefined;
    });

    describe('resolve', () => {
      it('should return IServiceDefinition returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceDefinition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultServiceDefinition).toEqual({ id: 123 });
      });

      it('should return new IServiceDefinition if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceDefinition = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultServiceDefinition).toEqual(new ServiceDefinition());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ServiceDefinition })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceDefinition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultServiceDefinition).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
