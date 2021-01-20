import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { SpecTypeComponent } from 'app/entities/spec-type/spec-type.component';
import { SpecTypeService } from 'app/entities/spec-type/spec-type.service';
import { SpecType } from 'app/shared/model/spec-type.model';

describe('Component Tests', () => {
  describe('SpecType Management Component', () => {
    let comp: SpecTypeComponent;
    let fixture: ComponentFixture<SpecTypeComponent>;
    let service: SpecTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SpecTypeComponent],
      })
        .overrideTemplate(SpecTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecTypeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecTypeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SpecType(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.specTypes && comp.specTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
