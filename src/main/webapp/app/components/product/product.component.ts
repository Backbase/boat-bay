import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { Observable, Subscription } from 'rxjs';
import { UiApiModule } from 'app/models/dashboard/v1';
import { ApiSpecsService } from 'app/services/api-specs.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit, OnDestroy {
  @Input() key!: string;
  @Input() name!: string;
  @Input() isCollapsible = false;
  @ViewChild(MatAccordion) accordion!: MatAccordion;

  public apiSpecs$!: Observable<UiApiModule[]>;
  public totalApi$!: Observable<number>;

  public hasSearchText$ = this.apiSpecsService.hasSearchText$;
  private subscription!: Subscription;

  constructor(private apiSpecsService: ApiSpecsService) {}

  ngOnInit(): void {
    this.apiSpecs$ = this.apiSpecsService.getApiModulesFor(this.key);
    this.totalApi$ = this.apiSpecsService.getTotalApiPerProduct(this.key);
    this.subscription = this.hasSearchText$.subscribe(value => {
      if (value) this.accordion.openAll();
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
