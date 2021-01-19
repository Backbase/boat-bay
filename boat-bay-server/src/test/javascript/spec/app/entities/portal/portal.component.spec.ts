import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { PortalComponent } from 'app/entities/portal/portal.component';
import { PortalService } from 'app/entities/portal/portal.service';
import { Portal } from 'app/shared/model/portal.model';

describe('Component Tests', () => {
  describe('Portal Management Component', () => {
    let comp: PortalComponent;
    let fixture: ComponentFixture<PortalComponent>;
    let service: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalComponent],
      })
        .overrideTemplate(PortalComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PortalService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Portal(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.portals && comp.portals[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
