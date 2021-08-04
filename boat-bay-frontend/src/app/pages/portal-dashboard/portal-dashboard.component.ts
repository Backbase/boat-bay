import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatPortal} from "../../services/dashboard/model/boatPortal";

@Component({
  selector: 'bb-dashboard',
  templateUrl: 'portal-dashboard.component.html',
  styleUrls: ['portal-dashboard.component.scss'],
})
export class PortalDashboardComponent implements OnInit {
  boatPortals$: Observable<BoatPortal[]>;

  constructor(protected boatDashboardService: DashboardHttpService) {
    this.boatPortals$ = boatDashboardService.getPortals().pipe(map(value => value));
  }

  ngOnInit(): void {
  }

}
