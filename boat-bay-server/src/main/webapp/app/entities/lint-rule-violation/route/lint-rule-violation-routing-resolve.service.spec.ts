jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILintRuleViolation, LintRuleViolation } from '../lint-rule-violation.model';
import { LintRuleViolationService } from '../service/lint-rule-violation.service';

import { LintRuleViolationRoutingResolveService } from './lint-rule-violation-routing-resolve.service';

describe('Service Tests', () => {
  describe('LintRuleViolation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LintRuleViolationRoutingResolveService;
    let service: LintRuleViolationService;
    let resultLintRuleViolation: ILintRuleViolation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LintRuleViolationRoutingResolveService);
      service = TestBed.inject(LintRuleViolationService);
      resultLintRuleViolation = undefined;
    });

    describe('resolve', () => {
      it('should return ILintRuleViolation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRuleViolation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintRuleViolation).toEqual({ id: 123 });
      });

      it('should return new ILintRuleViolation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRuleViolation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLintRuleViolation).toEqual(new LintRuleViolation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LintRuleViolation })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintRuleViolation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintRuleViolation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
