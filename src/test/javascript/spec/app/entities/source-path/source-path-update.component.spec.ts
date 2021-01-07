import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { SourcePathUpdateComponent } from 'app/entities/source-path/source-path-update.component';
import { SourcePathService } from 'app/entities/source-path/source-path.service';
import { SourcePath } from 'app/shared/model/source-path.model';

describe('Component Tests', () => {
  describe('SourcePath Management Update Component', () => {
    let comp: SourcePathUpdateComponent;
    let fixture: ComponentFixture<SourcePathUpdateComponent>;
    let service: SourcePathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [SourcePathUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SourcePathUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourcePathUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SourcePathService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SourcePath(123);
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
        const entity = new SourcePath();
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
