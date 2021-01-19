import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatCapability } from "../../models";


/**
 * Data source for the SimpleTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class CapabilityDatasource implements DataSource<BoatCapability> {

  private capabilitiesSubject = new BehaviorSubject<BoatCapability[]>([]);
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
  connect(collectionViewer: CollectionViewer): Observable<BoatCapability[]> {
    return this.capabilitiesSubject.asObservable();
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */

  disconnect(collectionViewer: CollectionViewer): void {
    this.capabilitiesSubject.complete();
    this.loadingSubject.complete();
  }

  loadCapabilitiesForProduct(portalKey: string, productKey: string, sortProperty: string, sortDirection: string, pageIndex = 0, pageSize = 3): void {
    console.log('Loading...')
    this.loadingSubject.next(true);
    this.boatService.getBoatCapabilities(portalKey, productKey, pageIndex, pageSize,  sortProperty,  sortDirection).pipe(
      map(response => {
        if(response.body) {
          this.countSubject.next(Number(response.headers.get('X-Total-Count')))
          this.capabilitiesSubject.next(response.body)
          this.loadingSubject.next(false)
        }
      })
    ).subscribe()
  }

}
