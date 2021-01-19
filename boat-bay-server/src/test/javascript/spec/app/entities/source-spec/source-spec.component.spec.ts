import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { SourceSpecComponent } from 'app/entities/source-spec/source-spec.component';
import { SourceSpecService } from 'app/entities/source-spec/source-spec.service';
import { SourceSpec } from 'app/shared/model/source-spec.model';

describe('Component Tests', () => {
  describe('SourceSpec Management Component', () => {
    let comp: SourceSpecComponent;
    let fixture: ComponentFixture<SourceSpecComponent>;
    let service: SourceSpecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourceSpecComponent],
      })
        .overrideTemplate(SourceSpecComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourceSpecComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SourceSpecService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SourceSpec(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sourceSpecs && comp.sourceSpecs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
