import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IService } from 'app/shared/model/service.model';

@Component({
  selector: 'jhi-service-detail',
  templateUrl: './service-detail.component.html',
})
export class ServiceDetailComponent implements OnInit {
  service: IService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ service }) => (this.service = service));
  }

  previousState(): void {
    window.history.back();
  }
}
