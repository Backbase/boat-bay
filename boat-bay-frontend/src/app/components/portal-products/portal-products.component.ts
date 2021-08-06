import {Component, Input, OnInit} from '@angular/core';
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {BoatPortal} from "../../services/dashboard/model/boatPortal";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-portal-products',
  templateUrl: './portal-products.component.html',
  styleUrls: ['./portal-products.component.scss']
})
export class PortalProductsComponent implements OnInit {

  @Input()
  set portal(portal: BoatPortal) {
    this._portal = portal;
  }

  private _portal!: BoatPortal;
  public $boatProducts!: Observable<BoatProduct[]>;

  constructor(private dashboardHttpService: DashboardHttpService) {

  }

  ngOnInit(): void {
    this.$boatProducts = this.dashboardHttpService.getPortalProducts({
      portalKey: this._portal.key,
      size: 100,
      page: 0,
      sort: ["name,asc"]
    });
  }

}
