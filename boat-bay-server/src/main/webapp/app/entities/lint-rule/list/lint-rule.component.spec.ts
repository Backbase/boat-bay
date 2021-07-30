import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LintRuleService } from '../service/lint-rule.service';

import { LintRuleComponent } from './lint-rule.component';

describe('Component Tests', () => {
  describe('LintRule Management Component', () => {
    let comp: LintRuleComponent;
    let fixture: ComponentFixture<LintRuleComponent>;
    let service: LintRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LintRuleComponent],
      })
        .overrideTemplate(LintRuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LintRuleComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LintRuleService);

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
      expect(comp.lintRules?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
