import { Component, EventEmitter, Output } from '@angular/core';
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { map, switchMap } from "rxjs/operators";
import { BoatCapability, BoatProduct, BoatService } from "../../models";
import { BoatRelease } from "../../models/boat-release";

export interface SpecFilter {

  capabilities?: BoatCapability[]
  services?: BoatService[]
  releases?: BoatRelease[]
  backwardsCompatible?: boolean

}

export interface CheckboxItem {
  name: string,
  disabled: boolean,
  checked: boolean,
  labelPosition: 'before' | 'after',
  value: BoatCapability | BoatService
}


@Component({
  selector: 'app-spec-filter',
  templateUrl: './spec-filter.component.html',
  styleUrls: ['./spec-filter.component.scss']
})
export class SpecFilterComponent {

  product$: Observable<BoatProduct>;

  public capabilities$: Observable<CheckboxItem[]>
  public services$: Observable<CheckboxItem[]>


  @Output() filter = new EventEmitter<SpecFilter>();

  private _filter: SpecFilter = {};


  constructor(protected dashboardService: BoatDashboardService, protected activatedRoute: ActivatedRoute) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));
    this.capabilities$ = this.product$.pipe(
      switchMap(product => this.dashboardService.getBoatCapabilities(product.portalKey, product.key, 0, 100, 'name', 'asc')),
      map(response => response.body ? response.body.map(capability => SpecFilterComponent.mapCapability(capability)) : []));
    this.services$ = this.product$.pipe(
      switchMap(product => this.dashboardService.getBoatServices(product.portalKey, product.key, 0, 100, 'name', 'asc')),
      map(response => response.body ? response.body.map(service => SpecFilterComponent.mapService(service)) : []));
  }

  private static mapCapability(capability: BoatCapability): CheckboxItem {
    return {
      name: capability.name,
      checked: false,
      disabled: false,
      labelPosition: 'after',
      value: capability
    };
  }

  private static mapService(service: BoatService): CheckboxItem {
    return {
      name: service.name,
      checked: false,
      disabled: false,
      labelPosition: 'after' ,
      value: service
    };
  }

  list_change() {


  }
}
