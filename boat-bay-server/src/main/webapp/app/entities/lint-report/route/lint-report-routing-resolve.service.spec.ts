jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILintReport, LintReport } from '../lint-report.model';
import { LintReportService } from '../service/lint-report.service';

import { LintReportRoutingResolveService } from './lint-report-routing-resolve.service';

describe('Service Tests', () => {
  describe('LintReport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LintReportRoutingResolveService;
    let service: LintReportService;
    let resultLintReport: ILintReport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LintReportRoutingResolveService);
      service = TestBed.inject(LintReportService);
      resultLintReport = undefined;
    });

    describe('resolve', () => {
      it('should return ILintReport returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintReport).toEqual({ id: 123 });
      });

      it('should return new ILintReport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintReport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLintReport).toEqual(new LintReport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LintReport })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLintReport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLintReport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
