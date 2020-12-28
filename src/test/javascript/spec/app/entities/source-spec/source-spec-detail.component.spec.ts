import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SourceSpecDetailComponent } from 'app/entities/source-spec/source-spec-detail.component';
import { SourceSpec } from 'app/shared/model/source-spec.model';

describe('Component Tests', () => {
  describe('SourceSpec Management Detail Component', () => {
    let comp: SourceSpecDetailComponent;
    let fixture: ComponentFixture<SourceSpecDetailComponent>;
    const route = ({ data: of({ sourceSpec: new SourceSpec(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourceSpecDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SourceSpecDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SourceSpecDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sourceSpec on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sourceSpec).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
