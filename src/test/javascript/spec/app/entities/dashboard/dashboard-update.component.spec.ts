import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BoatBayTestModule } from '../../../test.module';
import { DashboardUpdateComponent } from 'app/entities/dashboard/dashboard-update.component';
import { DashboardService } from 'app/entities/dashboard/dashboard.service';
import { Dashboard } from 'app/shared/model/dashboard.model';

describe('Component Tests', () => {
  describe('Dashboard Management Update Component', () => {
    let comp: DashboardUpdateComponent;
    let fixture: ComponentFixture<DashboardUpdateComponent>;
    let service: DashboardService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BoatBayTestModule],
        declarations: [DashboardUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DashboardUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DashboardUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DashboardService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dashboard(123);
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
        const entity = new Dashboard();
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
