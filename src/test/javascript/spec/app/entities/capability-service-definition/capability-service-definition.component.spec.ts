import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceDefinitionComponent } from 'app/entities/capability-service-definition/capability-service-definition.component';
import { CapabilityServiceDefinitionService } from 'app/entities/capability-service-definition/capability-service-definition.service';
import { CapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';

describe('Component Tests', () => {
  describe('CapabilityServiceDefinition Management Component', () => {
    let comp: CapabilityServiceDefinitionComponent;
    let fixture: ComponentFixture<CapabilityServiceDefinitionComponent>;
    let service: CapabilityServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceDefinitionComponent],
      })
        .overrideTemplate(CapabilityServiceDefinitionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CapabilityServiceDefinitionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CapabilityServiceDefinitionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new CapabilityServiceDefinition(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.capabilityServiceDefinitions && comp.capabilityServiceDefinitions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
