import { Routes } from '@angular/router';

import { HomeComponent } from './home.component';

// @Injectable({providedIn: 'root'})
// export class BoatPortalResolve implements Resolve<IPortal> {
//   constructor(protected portalService: PortalService, private router: Router) {
//   }
//
//   resolve(route: ActivatedRouteSnapshot): Observable<LintReport> | Promise<LintReport> | LintReport {
//     const key = route.params['key'];
//     if (key) {
//       return this.portalService.query({key: "id"}).pipe(
//         flatMap((response: HttpResponse<LintReport>) => {
//           if (response.body) {
//             return of(response.body);
//           } else {
//             this.router.navigate(['404']);
//             return EMPTY;
//           }
//         })
//       );
//     }
//     this.router.navigate(['404']);
//     return EMPTY;
//   }
// }

export const HOME_ROUTE: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      authorities: [],
      pageTitle: 'Boat Bay',
    },
  },
  // {
  //   path: 'dashboard/:key'
  //   data: {
  //     authorities: [Authority.USER],
  //     pageTitle: 'Lint Reports',
  //   },
  //   resolve: {
  //     lintReport: BoatPortalResolve,
  //   },
  //
  //
  // }
];
