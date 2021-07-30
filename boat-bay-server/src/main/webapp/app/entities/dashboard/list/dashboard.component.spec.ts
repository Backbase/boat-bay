import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DashboardService } from '../service/dashboard.service';

import { DashboardComponent } from './dashboard.component';

describe('Component Tests', () => {
  describe('Dashboard Management Component', () => {
    let comp: DashboardComponent;
    let fixture: ComponentFixture<DashboardComponent>;
    let service: DashboardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DashboardComponent],
      })
        .overrideTemplate(DashboardComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DashboardComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DashboardService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dashboards?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
