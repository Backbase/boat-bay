import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISourcePath } from '../source-path.model';
import { SourcePathService } from '../service/source-path.service';
import { SourcePathDeleteDialogComponent } from '../delete/source-path-delete-dialog.component';

@Component({
  selector: 'jhi-source-path',
  templateUrl: './source-path.component.html',
})
export class SourcePathComponent implements OnInit {
  sourcePaths?: ISourcePath[];
  isLoading = false;

  constructor(protected sourcePathService: SourcePathService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sourcePathService.query().subscribe(
      (res: HttpResponse<ISourcePath[]>) => {
        this.isLoading = false;
        this.sourcePaths = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISourcePath): number {
    return item.id!;
  }

  delete(sourcePath: ISourcePath): void {
    const modalRef = this.modalService.open(SourcePathDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sourcePath = sourcePath;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
