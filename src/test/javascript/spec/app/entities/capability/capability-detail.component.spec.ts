import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { CapabilityDetailComponent } from 'app/entities/capability/capability-detail.component';
import { Capability } from 'app/shared/model/capability.model';

describe('Component Tests', () => {
  describe('Capability Management Detail Component', () => {
    let comp: CapabilityDetailComponent;
    let fixture: ComponentFixture<CapabilityDetailComponent>;
    const route = ({ data: of({ capability: new Capability(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [CapabilityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CapabilityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CapabilityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load capability on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.capability).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
