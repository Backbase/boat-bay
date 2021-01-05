import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatAccordion } from '@angular/material/expansion';
import { Observable, Subscription } from 'rxjs';
import { UiApiModule } from 'app/models';
import { ApiSpecsService } from 'app/service/api-specs.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit, OnDestroy {
  @Input() name!: string;
  @Input() isCollapsible: boolean = false;
  @ViewChild(MatAccordion) accordion!: MatAccordion;

  public apiSpecs$!: Observable<UiApiModule[]>;
  public totalApi$!: Observable<number>;

  public hasSearchText$ = this.apiSpecsService.hasSearchText$;
  private subscription!: Subscription;

  constructor(private readonly apiSpecsService: ApiSpecsService) {}

  ngOnInit(): void {
    this.apiSpecs$ = this.apiSpecsService.getApiModulesFor(this.name);
    this.totalApi$ = this.apiSpecsService.getTotalApiPerProduct(this.name);
    this.subscription = this.hasSearchText$.subscribe(value => {
      if (value) this.accordion.openAll();
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
