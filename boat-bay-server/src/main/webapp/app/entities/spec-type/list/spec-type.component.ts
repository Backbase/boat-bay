import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecType } from '../spec-type.model';
import { SpecTypeService } from '../service/spec-type.service';
import { SpecTypeDeleteDialogComponent } from '../delete/spec-type-delete-dialog.component';

@Component({
  selector: 'jhi-spec-type',
  templateUrl: './spec-type.component.html',
})
export class SpecTypeComponent implements OnInit {
  specTypes?: ISpecType[];
  isLoading = false;

  constructor(protected specTypeService: SpecTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.specTypeService.query().subscribe(
      (res: HttpResponse<ISpecType[]>) => {
        this.isLoading = false;
        this.specTypes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISpecType): number {
    return item.id!;
  }

  delete(specType: ISpecType): void {
    const modalRef = this.modalService.open(SpecTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.specType = specType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
