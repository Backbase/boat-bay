import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PortalDashboardComponent } from "./pages/portal-dashboard/portal-dashboard.component";


const routes: Routes = [
  {
    path: '',
    component: PortalDashboardComponent
  },
  {
    path: 'lint-reports',
    loadChildren: () =>  import('./pages/lint-reports/lint-reports.module').then(m => m.LintReportsModule )
  },
  {
    path: 'diff-reports',
    loadChildren: () => import('./pages/diff-dashboard/diff-dashboard.module').then(m => m.DiffDashboardModule)
  },
  {
    path: 'tag-cloud',
    loadChildren: () => import('./pages/tag-dashboard/tag-dashboard.module').then(m => m.TagDashboardModule)
  },
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
