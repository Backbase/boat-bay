import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPortal } from 'app/shared/model/portal.model';

@Component({
  selector: 'jhi-portal-detail',
  templateUrl: './portal-detail.component.html',
})
export class PortalDetailComponent implements OnInit {
  portal: IPortal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ portal }) => (this.portal = portal));
  }

  previousState(): void {
    window.history.back();
  }
}
