import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { BoatProduct } from "../../models/";
import { BoatDashboardService } from "../../services/boat-dashboard.service";

@Component({
  selector: 'app-tag-cloud-dashboard',
  templateUrl: 'tag-cloud-dashboard.component.html',
  styleUrls: ['tag-cloud-dashboard.component.scss'],
})
export class TagCloudDashboardComponent implements OnInit {
  product$: Observable<BoatProduct>;

  constructor(protected activatedRoute: ActivatedRoute, protected dashboardService: BoatDashboardService) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));
  }

  ngOnInit(): void {
  }

}
