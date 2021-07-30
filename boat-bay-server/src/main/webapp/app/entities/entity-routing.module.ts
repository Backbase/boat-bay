import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dashboard',
        data: { pageTitle: 'boatbayApp.dashboard.home.title' },
        loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule),
      },
      {
        path: 'portal',
        data: { pageTitle: 'boatbayApp.portal.home.title' },
        loadChildren: () => import('./portal/portal.module').then(m => m.PortalModule),
      },
      {
        path: 'product-release',
        data: { pageTitle: 'boatbayApp.productRelease.home.title' },
        loadChildren: () => import('./product-release/product-release.module').then(m => m.ProductReleaseModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'boatbayApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'capability',
        data: { pageTitle: 'boatbayApp.capability.home.title' },
        loadChildren: () => import('./capability/capability.module').then(m => m.CapabilityModule),
      },
      {
        path: 'service-definition',
        data: { pageTitle: 'boatbayApp.serviceDefinition.home.title' },
        loadChildren: () => import('./service-definition/service-definition.module').then(m => m.ServiceDefinitionModule),
      },
      {
        path: 'spec',
        data: { pageTitle: 'boatbayApp.spec.home.title' },
        loadChildren: () => import('./spec/spec.module').then(m => m.SpecModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'boatbayApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'spec-type',
        data: { pageTitle: 'boatbayApp.specType.home.title' },
        loadChildren: () => import('./spec-type/spec-type.module').then(m => m.SpecTypeModule),
      },
      {
        path: 'source',
        data: { pageTitle: 'boatbayApp.source.home.title' },
        loadChildren: () => import('./source/source.module').then(m => m.SourceModule),
      },
      {
        path: 'source-path',
        data: { pageTitle: 'boatbayApp.sourcePath.home.title' },
        loadChildren: () => import('./source-path/source-path.module').then(m => m.SourcePathModule),
      },
      {
        path: 'lint-rule',
        data: { pageTitle: 'boatbayApp.lintRule.home.title' },
        loadChildren: () => import('./lint-rule/lint-rule.module').then(m => m.LintRuleModule),
      },
      {
        path: 'lint-report',
        data: { pageTitle: 'boatbayApp.lintReport.home.title' },
        loadChildren: () => import('./lint-report/lint-report.module').then(m => m.LintReportModule),
      },
      {
        path: 'lint-rule-violation',
        data: { pageTitle: 'boatbayApp.lintRuleViolation.home.title' },
        loadChildren: () => import('./lint-rule-violation/lint-rule-violation.module').then(m => m.LintRuleViolationModule),
      },
      {
        path: 'zally-config',
        data: { pageTitle: 'boatbayApp.zallyConfig.home.title' },
        loadChildren: () => import('./zally-config/zally-config.module').then(m => m.ZallyConfigModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
