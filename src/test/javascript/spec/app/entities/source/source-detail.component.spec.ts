import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SourceDetailComponent } from 'app/entities/source/source-detail.component';
import { Source } from 'app/shared/model/source.model';

describe('Component Tests', () => {
  describe('Source Management Detail Component', () => {
    let comp: SourceDetailComponent;
    let fixture: ComponentFixture<SourceDetailComponent>;
    const route = ({ data: of({ source: new Source(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SourceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SourceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load source on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.source).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
