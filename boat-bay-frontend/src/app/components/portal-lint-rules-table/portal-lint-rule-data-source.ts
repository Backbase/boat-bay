import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatLintRule } from "../../models";


/**
 * Data source for the SimpleTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class PortalLintRuleDataSource implements DataSource<BoatLintRule> {

  private servicesSubject = new BehaviorSubject<BoatLintRule[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public length = this.countSubject.asObservable();

  constructor(private boatService: BoatDashboardService) {
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(collectionViewer: CollectionViewer): Observable<BoatLintRule[]> {
    return this.servicesSubject.asObservable();
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */

  disconnect(collectionViewer: CollectionViewer): void {
    this.servicesSubject.complete();
    this.loadingSubject.complete();
  }

  loadLintRulesForPortal(portalKey: string):void {
    this.loadingSubject.next(true);
    this.boatService.getPortalLintRules(portalKey).pipe(
      map(response => {
        if(response.body) {
          this.countSubject.next(Number(response.headers.get('X-Total-Count')))
          this.servicesSubject.next(response.body)
          this.loadingSubject.next(false)
        }
      })
    ).subscribe()
  }

}
