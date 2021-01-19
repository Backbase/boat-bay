import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceComponent } from 'app/entities/capability-service/capability-service.component';
import { CapabilityServiceService } from 'app/entities/capability-service/capability-service.service';
import { CapabilityService } from 'app/shared/model/capability-service.model';

describe('Component Tests', () => {
  describe('CapabilityService Management Component', () => {
    let comp: CapabilityServiceComponent;
    let fixture: ComponentFixture<CapabilityServiceComponent>;
    let service: CapabilityServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceComponent],
      })
        .overrideTemplate(CapabilityServiceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityServiceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityServiceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CapabilityService(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.capabilityServices && comp.capabilityServices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
