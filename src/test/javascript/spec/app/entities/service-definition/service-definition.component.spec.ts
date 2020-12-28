import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { ServiceDefinitionComponent } from 'app/entities/service-definition/service-definition.component';
import { ServiceDefinitionService } from 'app/entities/service-definition/service-definition.service';
import { ServiceDefinition } from 'app/shared/model/service-definition.model';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Component', () => {
    let comp: ServiceDefinitionComponent;
    let fixture: ComponentFixture<ServiceDefinitionComponent>;
    let service: ServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ServiceDefinitionComponent],
      })
        .overrideTemplate(ServiceDefinitionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceDefinitionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ServiceDefinitionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ServiceDefinition(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.serviceDefinitions && comp.serviceDefinitions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
