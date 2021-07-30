import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PortalService } from '../service/portal.service';

import { PortalComponent } from './portal.component';

describe('Component Tests', () => {
  describe('Portal Management Component', () => {
    let comp: PortalComponent;
    let fixture: ComponentFixture<PortalComponent>;
    let service: PortalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PortalComponent],
      })
        .overrideTemplate(PortalComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PortalComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PortalService);

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
      expect(comp.portals?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
