import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SourcePathDetailComponent } from 'app/entities/source-path/source-path-detail.component';
import { SourcePath } from 'app/shared/model/source-path.model';

describe('Component Tests', () => {
  describe('SourcePath Management Detail Component', () => {
    let comp: SourcePathDetailComponent;
    let fixture: ComponentFixture<SourcePathDetailComponent>;
    const route = ({ data: of({ sourcePath: new SourcePath(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourcePathDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SourcePathDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SourcePathDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load sourcePath on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.sourcePath).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
