import { Component, Input, OnInit } from '@angular/core';
import { BoatDashboardService } from "../../services/boat-dashboard.service";
import { BoatProduct, BoatTag } from "../../models";
import { CloudData, CloudOptions } from 'angular-tag-cloud-module';

@Component({
  selector: 'app-spec-tag-cloud',
  template: '<angular-tag-cloud [data]="data" [config]="options"></angular-tag-cloud>',
  // template: '{{ data }}',
  styleUrls: ['spec-tag-cloud.component.scss']
})
export class SpecTagCloudComponent implements OnInit {

  public options: CloudOptions = {
    // if width is between 0 and 1 it will be set to the width of the upper element multiplied by the value
    width: 1000,
    // if height is between 0 and 1 it will be set to the height of the upper element multiplied by the value
    height: 600,
    overflow: false,

  };

  public data: CloudData[] = [];

  private _product!: BoatProduct;
  @Input()
  set product(product: BoatProduct) {
    this._product = product;
  }


  constructor(private dashboardService: BoatDashboardService) {
  }

  ngOnInit(): void {
    this.dashboardService.getTags(this._product.portalKey, this._product.key)
      .subscribe(({body}) =>
        this.data = !body ? [] : body.map((tag) => {
          return this.map(tag);
        }));

  }

  protected map(tag: BoatTag): CloudData {
    return {
      color: tag.color,
      text: tag.name,
      tooltip: tag.description
    };
  }
}
