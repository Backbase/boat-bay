import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SpecTypeService } from '../service/spec-type.service';

import { SpecTypeComponent } from './spec-type.component';

describe('Component Tests', () => {
  describe('SpecType Management Component', () => {
    let comp: SpecTypeComponent;
    let fixture: ComponentFixture<SpecTypeComponent>;
    let service: SpecTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpecTypeComponent],
      })
        .overrideTemplate(SpecTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecTypeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SpecTypeService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.specTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
