import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { SourcePathComponent } from 'app/entities/source-path/source-path.component';
import { SourcePathService } from 'app/entities/source-path/source-path.service';
import { SourcePath } from 'app/shared/model/source-path.model';

describe('Component Tests', () => {
  describe('SourcePath Management Component', () => {
    let comp: SourcePathComponent;
    let fixture: ComponentFixture<SourcePathComponent>;
    let service: SourcePathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourcePathComponent],
      })
        .overrideTemplate(SourcePathComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourcePathComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SourcePathService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new SourcePath(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.sourcePaths && comp.sourcePaths[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
