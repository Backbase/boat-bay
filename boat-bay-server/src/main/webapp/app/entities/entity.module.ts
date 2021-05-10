import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dashboard',
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.BoatBayDashboardModule),
      },
      {
        path: 'portal',
        loadChildren: () => import('./portal/portal.module').then(m => m.BoatBayPortalModule),
      },
      {
        path: 'product-release',
        loadChildren: () => import('./product-release/product-release.module').then(m => m.BoatBayProductReleaseModule),
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.BoatBayProductModule),
      },
      {
        path: 'capability',
        loadChildren: () => import('./capability/capability.module').then(m => m.BoatBayCapabilityModule),
      },
      {
        path: 'service-definition',
        loadChildren: () => import('./service-definition/service-definition.module').then(m => m.BoatBayServiceDefinitionModule),
      },
      {
        path: 'spec',
        loadChildren: () => import('./spec/spec.module').then(m => m.BoatBaySpecModule),
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.BoatBayTagModule),
      },
      {
        path: 'spec-type',
        loadChildren: () => import('./spec-type/spec-type.module').then(m => m.BoatBaySpecTypeModule),
      },
      {
        path: 'source',
        loadChildren: () => import('./source/source.module').then(m => m.BoatBaySourceModule),
      },
      {
        path: 'source-path',
        loadChildren: () => import('./source-path/source-path.module').then(m => m.BoatBaySourcePathModule),
      },
      {
        path: 'lint-rule',
        loadChildren: () => import('./lint-rule/lint-rule.module').then(m => m.BoatBayLintRuleModule),
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
        path: 'zally-config',
        loadChildren: () => import('./zally-config/zally-config.module').then(m => m.BoatBayZallyConfigModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BoatBayEntityModule {}
