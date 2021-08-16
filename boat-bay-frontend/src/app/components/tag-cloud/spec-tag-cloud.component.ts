import {Component, Input, OnInit} from '@angular/core';
import {CloudData, CloudOptions} from 'angular-tag-cloud-module';
import {BoatProduct} from "../../services/dashboard/model/boatProduct";
import {DashboardHttpService} from "../../services/dashboard/api/dashboard.service";
import {BoatTag} from "../../services/dashboard/model/boatTag";

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


  constructor(private dashboardService: DashboardHttpService) {
  }

  ngOnInit(): void {
    this.dashboardService.getProductTags({
      portalKey: this._product.portalKey,
      productKey: this._product.key
    }, "response")
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
