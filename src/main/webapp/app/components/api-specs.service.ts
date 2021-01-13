import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable, throwError } from 'rxjs';
import { catchError, first, map, shareReplay } from 'rxjs/operators';

import { ApiModule, LegacyPortalView, Product, UiApiModule } from 'app/models/dashboard/v1';
import { NAVIGATION_FILE_PATH } from '../tokens';
import { BoatDashboardService } from 'app/services/boat-dashboard.service';
import { BoatTagManagerService } from 'app/services/boat-tag-manager.service';
import { HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiSpecsService {
  private readonly currentReleaseVersion$$ = new BehaviorSubject<string>('');
  public readonly currentReleaseVersion$ = this.currentReleaseVersion$$.asObservable();

  private readonly searchQuery$$ = new BehaviorSubject<string>('');
  public readonly searchQuery$: Observable<string> = this.searchQuery$$.asObservable();

  public readonly hasSearchText$: Observable<boolean> = this.searchQuery$$.pipe(map(query => query !== null && query.length > 0));

  private readonly productTags = ['retail', 'business', 'wealth', 'foundation', 'identity', 'flow', 'basic support'];

  private readonly dashboardView = this.dashboardViewService.getLegacyPortalView().pipe(
    shareReplay({
      bufferSize: 1,
      refCount: true,
    }),
    catchError(() => throwError('Dashboard View could not be loaded'))
  );

  private readonly apiModules$: Observable<[string, ApiModule][]> = this.dashboardView.pipe(
    map((value: LegacyPortalView) => {
      return Object.values(value.capabilities)
        .map(({ modules }) => Object.entries(modules))
        .flat();
    })
  );

  private readonly releases$ = this.dashboardView.pipe(
    map((value: LegacyPortalView) => {
      return value.releases;
    })
  );

  public readonly filteredApiModules$: Observable<Map<string, ApiModule>> = combineLatest([this.apiModules$, this.searchQuery$$]).pipe(
    map(([apiModules, searchQuery]: [[string, ApiModule][], string]) => {
      const formattedSearchQuery = searchQuery.trim().toLowerCase();

      const filteredApiModules: [string, ApiModule][] = apiModules.filter(([, { name, tags }]) => {
        const isTitleMathSearchQuery = name.toLowerCase().includes(formattedSearchQuery);
        const isTagsMatchSearchQuery = tags.some(tag => tag.toLowerCase().includes(formattedSearchQuery));

        return isTitleMathSearchQuery || isTagsMatchSearchQuery;
      });

      return new Map<string, ApiModule>(filteredApiModules);
    })
  );

  public readonly products$: Observable<Product[]> = this.dashboardView.pipe(
    map((value: LegacyPortalView) => {
      return Object.values(value.products);
    })
  );

  public readonly availableReleaseVersions$: Observable<string[]> = this.dashboardView.pipe(
    map((value: LegacyPortalView) => {
      return Object.keys(value.releases);
    })
  );

  constructor(
    @Inject(NAVIGATION_FILE_PATH) private navigationFilePath: string,
    private dashboardViewService: BoatDashboardService,
    private tagService: BoatTagManagerService
  ) {
    this.availableReleaseVersions$.pipe(first()).subscribe(releaseVersions => this.selectCurrentReleaseVersion(releaseVersions[0]));
  }

  public getSpecColorAsPerTags(specTags: string[]): string {
    const productTagInSpec = specTags.filter(tag => this.productTags.includes(tag.toLowerCase()));

    if (productTagInSpec.length === 0) return 'default';
    if (productTagInSpec.length > 1) return 'mixed';

    return productTagInSpec[0].toLowerCase() === 'basic support' ? 'basic-support' : productTagInSpec[0].toLowerCase();
  }

  public setSearchQuery(text: string): void {
    this.searchQuery$$.next(text);
  }

  public getApiModulesFor(product: string): Observable<UiApiModule[]> {
    return combineLatest([this.currentReleaseVersion$$, this.releases$, this.filteredApiModules$]).pipe(
      map(([selectedVersion, releases, apiModulesMap]) => {
        const productsForSelectedRelease = releases[selectedVersion];
        const productRelease = productsForSelectedRelease[product];
        const servicesForSelectedProduct = productRelease.services;
        const specsForSelectedProduct = productRelease.specs;

        return this.constructApiStructureForModules(servicesForSelectedProduct, specsForSelectedProduct, apiModulesMap);
      })
    );
  }

  private constructApiStructureForModules(
    servicesForSelectedProduct: UiApiModule[],
    specsForSelectedProduct: number[],
    apiModulesMap: Map<String, ApiModule>
  ): UiApiModule[] {
    const moduleNames = Object.entries(servicesForSelectedProduct);

    return moduleNames.reduce((listOfApiModules: UiApiModule[], [moduleName, moduleVersion]: [string, UiApiModule]) => {
      const api = apiModulesMap.get(moduleName);
      if (api) {
        listOfApiModules.push({
          key: api.key,
          name: api.name,
          description: api.description,
          tags: api.tags,
          icon: api['x-icon'] || '',
          version: moduleVersion.version,
          portalPath: moduleVersion.version,
          specs: Object.values(api.specs).filter(spec => specsForSelectedProduct.includes(spec.id)),
        });
      }

      return listOfApiModules;
    }, []);
  }

  public getTotalApiPerProduct(product: string): Observable<number> {
    return combineLatest([this.currentReleaseVersion$$, this.releases$]).pipe(
      map(([selectedVersion, releases]) => {
        const formattedProductName = product.trim().toLowerCase();
        const productsForSelectedReleas = releases[selectedVersion];
        const productModulesDict = productsForSelectedReleas[formattedProductName].modules;
        const amountOfModulesPerProduct = Object.keys(productModulesDict).length;

        return amountOfModulesPerProduct;
      })
    );
  }

  public selectCurrentReleaseVersion(version: string): void {
    this.currentReleaseVersion$$.next(version);
  }

  public hideTag(tag: string): Observable<HttpResponse<any>> {
    return this.tagService.hide(tag);
  }
}