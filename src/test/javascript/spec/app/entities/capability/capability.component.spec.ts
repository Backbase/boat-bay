import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityComponent } from 'app/entities/capability/capability.component';
import { CapabilityService } from 'app/entities/capability/capability.service';
import { Capability } from 'app/shared/model/capability.model';

describe('Component Tests', () => {
  describe('Capability Management Component', () => {
    let comp: CapabilityComponent;
    let fixture: ComponentFixture<CapabilityComponent>;
    let service: CapabilityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityComponent],
      })
        .overrideTemplate(CapabilityComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Capability(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.capabilities && comp.capabilities[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
