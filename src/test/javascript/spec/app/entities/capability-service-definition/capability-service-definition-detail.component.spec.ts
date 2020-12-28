import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceDefinitionDetailComponent } from 'app/entities/capability-service-definition/capability-service-definition-detail.component';
import { CapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';

describe('Component Tests', () => {
  describe('CapabilityServiceDefinition Management Detail Component', () => {
    let comp: CapabilityServiceDefinitionDetailComponent;
    let fixture: ComponentFixture<CapabilityServiceDefinitionDetailComponent>;
    const route = ({ data: of({ capabilityServiceDefinition: new CapabilityServiceDefinition(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceDefinitionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CapabilityServiceDefinitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CapabilityServiceDefinitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load capabilityServiceDefinition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.capabilityServiceDefinition).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
