import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICapabilityServiceDefinition } from 'app/shared/model/capability-service-definition.model';

@Component({
  selector: 'jhi-capability-service-definition-detail',
  templateUrl: './capability-service-definition-detail.component.html',
})
export class CapabilityServiceDefinitionDetailComponent implements OnInit {
  capabilityServiceDefinition: ICapabilityServiceDefinition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(
      ({ capabilityServiceDefinition }) => (this.capabilityServiceDefinition = capabilityServiceDefinition)
    );
  }

  previousState(): void {
    window.history.back();
  }
}
