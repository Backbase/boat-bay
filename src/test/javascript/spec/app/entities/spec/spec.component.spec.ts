import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { SpecComponent } from 'app/entities/spec/spec.component';
import { SpecService } from 'app/entities/spec/spec.service';
import { Spec } from 'app/shared/model/spec.model';

describe('Component Tests', () => {
  describe('Spec Management Component', () => {
    let comp: SpecComponent;
    let fixture: ComponentFixture<SpecComponent>;
    let service: SpecService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecComponent],
      })
        .overrideTemplate(SpecComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Spec(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.specs && comp.specs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
