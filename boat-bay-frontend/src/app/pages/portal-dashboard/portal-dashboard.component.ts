import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BoatDashboard, BoatPortal } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatLintReportService } from "../../services/boat-lint-report.service";

@Component({
  selector: 'bb-dashboard',
  templateUrl: 'portal-dashboard.component.html',
  styleUrls: ['portal-dashboard.component.scss'],
})
export class PortalDashboardComponent implements OnInit {
  boatPortals$: Observable<BoatDashboard[]>;
  portals!: BoatPortal[] | null;

  constructor(
    protected boatDashboardService: BoatDashboardService,
    protected boatLintReportService: BoatLintReportService,
  ) {
    boatDashboardService.getPortals().subscribe(
      value => {
        this.portals = value.body;
      }
    );
    this.boatPortals$ = boatDashboardService.getBoatPortalView().pipe(map(value => value));
  }

  ngOnInit(): void {
  }

  analyse(portal: BoatDashboard): void {
    this.boatLintReportService.postLintProduct(portal.productId).subscribe();
  }
}
