import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceDefinition } from '../service-definition.model';
import { ServiceDefinitionService } from '../service/service-definition.service';
import { ServiceDefinitionDeleteDialogComponent } from '../delete/service-definition-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-service-definition',
  templateUrl: './service-definition.component.html',
})
export class ServiceDefinitionComponent implements OnInit {
  serviceDefinitions?: IServiceDefinition[];
  isLoading = false;

  constructor(
    protected serviceDefinitionService: ServiceDefinitionService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.serviceDefinitionService.query().subscribe(
      (res: HttpResponse<IServiceDefinition[]>) => {
        this.isLoading = false;
        this.serviceDefinitions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IServiceDefinition): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(serviceDefinition: IServiceDefinition): void {
    const modalRef = this.modalService.open(ServiceDefinitionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.serviceDefinition = serviceDefinition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
