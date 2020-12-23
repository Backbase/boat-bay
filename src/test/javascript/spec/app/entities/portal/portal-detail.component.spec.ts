import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { PortalDetailComponent } from 'app/entities/portal/portal-detail.component';
import { Portal } from 'app/shared/model/portal.model';

describe('Component Tests', () => {
  describe('Portal Management Detail Component', () => {
    let comp: PortalDetailComponent;
    let fixture: ComponentFixture<PortalDetailComponent>;
    const route = ({ data: of({ portal: new Portal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [PortalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PortalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PortalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load portal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.portal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
