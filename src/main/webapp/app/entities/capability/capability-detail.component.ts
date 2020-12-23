import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICapability } from 'app/shared/model/capability.model';

@Component({
  selector: 'jhi-capability-detail',
  templateUrl: './capability-detail.component.html',
})
export class CapabilityDetailComponent implements OnInit {
  capability: ICapability | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ capability }) => (this.capability = capability));
  }

  previousState(): void {
    window.history.back();
  }
}
