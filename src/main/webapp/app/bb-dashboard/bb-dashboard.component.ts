import { Component, OnInit } from '@angular/core';
import { PortalService } from 'app/entities/portal/portal.service';
import { IPortal } from 'app/shared/model/portal.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'bb-dashboard',
  templateUrl: './bb-dashboard.component.html',
  styleUrls: ['./bb-dashboard.component.scss'],
})
export class BbDashboardComponent implements OnInit {
  portals?: IPortal[];

  constructor(protected portalService: PortalService) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.portalService.query().subscribe((res: HttpResponse<IPortal[]>) => (this.portals = res.body || []));
  }
}
