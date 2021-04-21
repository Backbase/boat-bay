import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BoatBayTestModule } from '../../../test.module';
import { ZallyConfigComponent } from 'app/entities/zally-config/zally-config.component';
import { ZallyConfigService } from 'app/entities/zally-config/zally-config.service';
import { ZallyConfig } from 'app/shared/model/zally-config.model';

describe('Component Tests', () => {
  describe('ZallyConfig Management Component', () => {
    let comp: ZallyConfigComponent;
    let fixture: ComponentFixture<ZallyConfigComponent>;
    let service: ZallyConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ZallyConfigComponent],
      })
        .overrideTemplate(ZallyConfigComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ZallyConfigComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ZallyConfigService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ZallyConfig(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.zallyConfigs && comp.zallyConfigs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
