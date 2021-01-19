import { Component } from '@angular/core';
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { ActivatedRoute } from "@angular/router";
import { FormControl } from "@angular/forms";
import { Observable } from "rxjs";
import { map, switchMap } from "rxjs/operators";
import { BoatCapability, BoatProduct, BoatService } from "../../models";

@Component({
  selector: 'app-spec-filter',
  templateUrl: './spec-filter.component.html',
  styleUrls: ['./spec-filter.component.scss']
})
export class SpecFilterComponent {

  product$: Observable<BoatProduct>;

  public capabilities$: Observable<BoatCapability[]>
  public services$: Observable<BoatService[]>

  selectedCapabilities = new FormControl();
  selectedServices = new FormControl();

  constructor(private dashboardService: BoatDashboardService, protected activatedRoute: ActivatedRoute) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));
    this.capabilities$ = this.product$.pipe(
      switchMap(product => this.dashboardService.getBoatCapabilities(product.portalKey, product.key, 0, 100, 'name', 'asc')),
      map(response => response.body ? response.body : []))
    this.services$ = this.product$.pipe(
      switchMap(product => this.dashboardService.getBoatServices(product.portalKey, product.key, 0, 100, 'name', 'asc')),
      map(response => response.body ? response.body : []))
  }

}
