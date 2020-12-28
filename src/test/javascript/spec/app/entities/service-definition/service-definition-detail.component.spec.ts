import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { ServiceDefinitionDetailComponent } from 'app/entities/service-definition/service-definition-detail.component';
import { ServiceDefinition } from 'app/shared/model/service-definition.model';

describe('Component Tests', () => {
  describe('ServiceDefinition Management Detail Component', () => {
    let comp: ServiceDefinitionDetailComponent;
    let fixture: ComponentFixture<ServiceDefinitionDetailComponent>;
    const route = ({ data: of({ serviceDefinition: new ServiceDefinition(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ServiceDefinitionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ServiceDefinitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServiceDefinitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load serviceDefinition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serviceDefinition).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
