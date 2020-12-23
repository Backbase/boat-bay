import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { ServiceComponent } from 'app/entities/service/service.component';
import { ServiceService } from 'app/entities/service/service.service';
import { Service } from 'app/shared/model/service.model';

describe('Component Tests', () => {
  describe('Service Management Component', () => {
    let comp: ServiceComponent;
    let fixture: ComponentFixture<ServiceComponent>;
    let service: ServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ServiceComponent],
      })
        .overrideTemplate(ServiceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServiceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Service(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.services && comp.services[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
