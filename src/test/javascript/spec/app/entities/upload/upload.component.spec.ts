import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { UploadComponent } from 'app/entities/upload/upload.component';
import { UploadService } from 'app/entities/upload/upload.service';
import { Upload } from 'app/shared/model/upload.model';

describe('Component Tests', () => {
  describe('Upload Management Component', () => {
    let comp: UploadComponent;
    let fixture: ComponentFixture<UploadComponent>;
    let service: UploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [UploadComponent],
      })
        .overrideTemplate(UploadComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UploadComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UploadService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Upload(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.uploads && comp.uploads[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
