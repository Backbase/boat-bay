import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityServiceDetailComponent } from 'app/entities/capability-service/capability-service-detail.component';
import { CapabilityService } from 'app/shared/model/capability-service.model';

describe('Component Tests', () => {
  describe('CapabilityService Management Detail Component', () => {
    let comp: CapabilityServiceDetailComponent;
    let fixture: ComponentFixture<CapabilityServiceDetailComponent>;
    const route = ({ data: of({ capabilityService: new CapabilityService(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityServiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CapabilityServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CapabilityServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load capabilityService on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.capabilityService).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
