import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { ZallyConfigUpdateComponent } from 'app/entities/zally-config/zally-config-update.component';
import { ZallyConfigService } from 'app/entities/zally-config/zally-config.service';
import { ZallyConfig } from 'app/shared/model/zally-config.model';

describe('Component Tests', () => {
  describe('ZallyConfig Management Update Component', () => {
    let comp: ZallyConfigUpdateComponent;
    let fixture: ComponentFixture<ZallyConfigUpdateComponent>;
    let service: ZallyConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [ZallyConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ZallyConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ZallyConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ZallyConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ZallyConfig(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ZallyConfig();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
