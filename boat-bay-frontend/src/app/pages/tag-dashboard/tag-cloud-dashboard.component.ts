import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatProduct} from "../../services/dashboard/model/boatProduct";

@Component({
  selector: 'app-tag-cloud-dashboard',
  templateUrl: 'tag-cloud-dashboard.component.html',
  styleUrls: ['tag-cloud-dashboard.component.scss'],
})
export class TagCloudDashboardComponent implements OnInit {
  product$: Observable<BoatProduct> | null;

  constructor(protected activatedRoute: ActivatedRoute, protected dashboardService: DashboardHttpService) {
    this.product$ = activatedRoute.data.pipe(map(({product}) => product));
  }

  ngOnInit(): void {
  }

}
