import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceDefinition } from 'app/shared/model/service-definition.model';

@Component({
  selector: 'jhi-service-definition-detail',
  templateUrl: './service-definition-detail.component.html',
})
export class ServiceDefinitionDetailComponent implements OnInit {
  serviceDefinition: IServiceDefinition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceDefinition }) => (this.serviceDefinition = serviceDefinition));
  }

  previousState(): void {
    window.history.back();
  }
}
