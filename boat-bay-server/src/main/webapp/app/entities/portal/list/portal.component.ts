import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPortal } from '../portal.model';
import { PortalService } from '../service/portal.service';
import { PortalDeleteDialogComponent } from '../delete/portal-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-portal',
  templateUrl: './portal.component.html',
})
export class PortalComponent implements OnInit {
  portals?: IPortal[];
  isLoading = false;

  constructor(protected portalService: PortalService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.portalService.query().subscribe(
      (res: HttpResponse<IPortal[]>) => {
        this.isLoading = false;
        this.portals = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPortal): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(portal: IPortal): void {
    const modalRef = this.modalService.open(PortalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.portal = portal;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
