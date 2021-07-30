import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ZallyConfigService } from '../service/zally-config.service';

import { ZallyConfigComponent } from './zally-config.component';

describe('Component Tests', () => {
  describe('ZallyConfig Management Component', () => {
    let comp: ZallyConfigComponent;
    let fixture: ComponentFixture<ZallyConfigComponent>;
    let service: ZallyConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ZallyConfigComponent],
      })
        .overrideTemplate(ZallyConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ZallyConfigComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ZallyConfigService);

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
      expect(comp.zallyConfigs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
