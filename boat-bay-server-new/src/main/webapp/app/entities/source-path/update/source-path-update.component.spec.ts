jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SourcePathService } from '../service/source-path.service';
import { ISourcePath, SourcePath } from '../source-path.model';
import { ISource } from 'app/entities/source/source.model';
import { SourceService } from 'app/entities/source/service/source.service';

import { SourcePathUpdateComponent } from './source-path-update.component';

describe('Component Tests', () => {
  describe('SourcePath Management Update Component', () => {
    let comp: SourcePathUpdateComponent;
    let fixture: ComponentFixture<SourcePathUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sourcePathService: SourcePathService;
    let sourceService: SourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SourcePathUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SourcePathUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SourcePathUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sourcePathService = TestBed.inject(SourcePathService);
      sourceService = TestBed.inject(SourceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Source query and add missing value', () => {
        const sourcePath: ISourcePath = { id: 456 };
        const source: ISource = { id: 93353 };
        sourcePath.source = source;

        const sourceCollection: ISource[] = [{ id: 99620 }];
        jest.spyOn(sourceService, 'query').mockReturnValue(of(new HttpResponse({ body: sourceCollection })));
        const additionalSources = [source];
        const expectedCollection: ISource[] = [...additionalSources, ...sourceCollection];
        jest.spyOn(sourceService, 'addSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ sourcePath });
        comp.ngOnInit();

        expect(sourceService.query).toHaveBeenCalled();
        expect(sourceService.addSourceToCollectionIfMissing).toHaveBeenCalledWith(sourceCollection, ...additionalSources);
        expect(comp.sourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sourcePath: ISourcePath = { id: 456 };
        const source: ISource = { id: 18110 };
        sourcePath.source = source;

        activatedRoute.data = of({ sourcePath });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sourcePath));
        expect(comp.sourcesSharedCollection).toContain(source);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SourcePath>>();
        const sourcePath = { id: 123 };
        jest.spyOn(sourcePathService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sourcePath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sourcePath }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sourcePathService.update).toHaveBeenCalledWith(sourcePath);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SourcePath>>();
        const sourcePath = new SourcePath();
        jest.spyOn(sourcePathService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sourcePath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sourcePath }));
        saveSubject.complete();

        // THEN
        expect(sourcePathService.create).toHaveBeenCalledWith(sourcePath);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<SourcePath>>();
        const sourcePath = { id: 123 };
        jest.spyOn(sourcePathService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sourcePath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sourcePathService.update).toHaveBeenCalledWith(sourcePath);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSourceById', () => {
        it('Should return tracked Source primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSourceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
