jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILintRule, LintRule } from '../lint-rule.model';
import { LintRuleService } from '../service/lint-rule.service';

import { LintRuleRoutingResolveService } from './lint-rule-routing-resolve.service';

describe('Service Tests', () => {
  describe('LintRule routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LintRuleRoutingResolveService;
    let service: LintRuleService;
    let resultLintRule: ILintRule | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LintRuleRoutingResolveService);
      service = TestBed.inject(LintRuleService);
      resultLintRule = undefined;
    });

    describe('resolve', () => {
      it('should return ILintRule returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintRule).toEqual({ id: 123 });
      });

      it('should return new ILintRule if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRule = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLintRule).toEqual(new LintRule());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LintRule })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintRule).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
