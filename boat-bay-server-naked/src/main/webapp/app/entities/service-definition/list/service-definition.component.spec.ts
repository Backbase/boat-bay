import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ServiceDefinitionService } from '../service/service-definition.service';

import { ServiceDefinitionComponent } from './service-definition.component';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Component', () => {
    let comp: ServiceDefinitionComponent;
    let fixture: ComponentFixture<ServiceDefinitionComponent>;
    let service: ServiceDefinitionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServiceDefinitionComponent],
      })
        .overrideTemplate(ServiceDefinitionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceDefinitionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ServiceDefinitionService);

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
      expect(comp.serviceDefinitions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
