import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SourceService } from '../service/source.service';

import { SourceComponent } from './source.component';

describe('Component Tests', () => {
  describe('Source Management Component', () => {
    let comp: SourceComponent;
    let fixture: ComponentFixture<SourceComponent>;
    let service: SourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SourceComponent],
      })
        .overrideTemplate(SourceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SourceService);

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
      expect(comp.sources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
