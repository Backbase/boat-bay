import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'portal',
        loadChildren: () => import('./portal/portal.module').then(m => m.BoatBayPortalModule),
      },
      {
        path: 'capability',
        loadChildren: () => import('./capability/capability.module').then(m => m.BoatBayCapabilityModule),
      },
      {
        path: 'spec',
        loadChildren: () => import('./spec/spec.module').then(m => m.BoatBaySpecModule),
      },
      {
        path: 'lint-rule',
        loadChildren: () => import('./lint-rule/lint-rule.module').then(m => m.BoatBayLintRuleModule),
      },
      {
        path: 'lint-rule-set',
        loadChildren: () => import('./lint-rule-set/lint-rule-set.module').then(m => m.BoatBayLintRuleSetModule),
      },
      {
        path: 'lint-report',
        loadChildren: () => import('./lint-report/lint-report.module').then(m => m.BoatBayLintReportModule),
      },
      {
        path: 'lint-rule-violation',
        loadChildren: () => import('./lint-rule-violation/lint-rule-violation.module').then(m => m.BoatBayLintRuleViolationModule),
      },
      {
        path: 'source',
        loadChildren: () => import('./source/source.module').then(m => m.BoatBaySourceModule),
      },
      {
        path: 'service-definition',
        loadChildren: () => import('./service-definition/service-definition.module').then(m => m.BoatBayServiceDefinitionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BoatBayEntityModule {}
