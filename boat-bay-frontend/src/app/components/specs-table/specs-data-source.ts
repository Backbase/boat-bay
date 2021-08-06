import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from "rxjs/operators";
import {SpecFilter} from "./specs-table.component";
import {BoatSpec} from "../../services/dashboard/model/boatSpec";
import {DashboardHttpService, GetPortalSpecsRequestParams} from "../../services/dashboard/api/dashboard.service";


/**
 * Data source for the SimpleTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class SpecsDataSource implements DataSource<BoatSpec> {

  private servicesSubject = new BehaviorSubject<BoatSpec[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);

  public loading$ = this.loadingSubject.asObservable();
  public length = this.countSubject.asObservable();

  constructor(private boatService: DashboardHttpService) {
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(collectionViewer: CollectionViewer): Observable<BoatSpec[]> {
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

  loadSpecs(specFilter: SpecFilter, sortProperty: string, sortDirection: string, pageIndex = 0, pageSize = 3): void {
    this.loadingSubject.next(true);

    let requestParmas: GetPortalSpecsRequestParams = {
      portalKey: specFilter.portalKey,
      productKey: specFilter.productKey,
      page: pageIndex,
      size: pageSize,
      sort: [sortDirection + "," + sortDirection],
      capabilityKeys: specFilter.capabilities != null ? specFilter.capabilities.map(value => value.key) : undefined,
      serviceKeys: specFilter.services != null ? specFilter.services.map(value => value.key) : undefined,
      productReleaseKey: specFilter.release != null ? specFilter.release.key : undefined
    }

    this.boatService.getPortalSpecs(requestParmas, "response").pipe(
      map(response => {
        if (response.body) {
          this.countSubject.next(Number(response.headers.get('X-Total-Count')))
          this.servicesSubject.next(response.body)
          this.loadingSubject.next(false)
        }
      })
    ).subscribe()
  }

}
