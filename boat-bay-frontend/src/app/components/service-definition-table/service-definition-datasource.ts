import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { BoatService } from "../../models";
import { BoatDashboardService } from "../../services/boat-dashboard.service";


/**
 * Data source for the SimpleTable view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class ServiceDefinitionDatasource implements DataSource<BoatService> {

  private servicesSubject = new BehaviorSubject<BoatService[]>([]);
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
  connect(collectionViewer: CollectionViewer): Observable<BoatService[]> {
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

  loadServicesForProduct(portalKey: string, productKey: string, sortProperty: string, sortDirection: string, pageIndex = 0, pageSize = 3): void {
    this.loadingSubject.next(true);
    this.boatService.getBoatServices(portalKey, productKey, pageIndex, pageSize, sortProperty, sortDirection).pipe(
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
