import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { DashboardComponent } from 'app/entities/dashboard/dashboard.component';
import { DashboardService } from 'app/entities/dashboard/dashboard.service';
import { Dashboard } from 'app/shared/model/dashboard.model';

describe('Component Tests', () => {
  describe('Dashboard Management Component', () => {
    let comp: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;
    let service: DashboardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [DashboardComponent],
      })
        .overrideTemplate(DashboardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DashboardComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DashboardService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Dashboard(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dashboards && comp.dashboards[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
