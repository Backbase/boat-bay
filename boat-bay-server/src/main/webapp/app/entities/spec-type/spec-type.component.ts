import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecType } from 'app/shared/model/spec-type.model';
import { SpecTypeService } from './spec-type.service';
import { SpecTypeDeleteDialogComponent } from './spec-type-delete-dialog.component';

@Component({
  selector: 'jhi-spec-type',
  templateUrl: './spec-type.component.html',
})
export class SpecTypeComponent implements OnInit, OnDestroy {
  specTypes?: ISpecType[];
  eventSubscriber?: Subscription;

  constructor(protected specTypeService: SpecTypeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.specTypeService.query().subscribe((res: HttpResponse<ISpecType[]>) => (this.specTypes = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSpecTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISpecType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSpecTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('specTypeListModification', () => this.loadAll());
  }

  delete(specType: ISpecType): void {
    const modalRef = this.modalService.open(SpecTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.specType = specType;
  }
}
