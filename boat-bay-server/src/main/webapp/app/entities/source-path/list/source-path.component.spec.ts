import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SourcePathService } from '../service/source-path.service';

import { SourcePathComponent } from './source-path.component';

describe('Component Tests', () => {
  describe('SourcePath Management Component', () => {
    let comp: SourcePathComponent;
    let fixture: ComponentFixture<SourcePathComponent>;
    let service: SourcePathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SourcePathComponent],
      })
        .overrideTemplate(SourcePathComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourcePathComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SourcePathService);

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
      expect(comp.sourcePaths?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
