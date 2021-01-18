import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IServiceDefinition } from 'app/shared/model/service-definition.model';

@Component({
  selector: 'jhi-service-definition-detail',
  templateUrl: './service-definition-detail.component.html',
})
export class ServiceDefinitionDetailComponent implements OnInit {
  serviceDefinition: IServiceDefinition | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceDefinition }) => (this.serviceDefinition = serviceDefinition));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
