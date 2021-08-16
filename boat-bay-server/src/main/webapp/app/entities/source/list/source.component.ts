import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISource } from '../source.model';
import { SourceService } from '../service/source.service';
import { SourceDeleteDialogComponent } from '../delete/source-delete-dialog.component';

@Component({
  selector: 'jhi-source',
  templateUrl: './source.component.html',
})
export class SourceComponent implements OnInit {
  sources?: ISource[];
  isLoading = false;

  constructor(protected sourceService: SourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sourceService.query().subscribe(
      (res: HttpResponse<ISource[]>) => {
        this.isLoading = false;
        this.sources = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISource): number {
    return item.id!;
  }

  delete(source: ISource): void {
    const modalRef = this.modalService.open(SourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.source = source;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
