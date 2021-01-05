import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { BoatBaySharedModule } from 'app/shared/shared.module';
import { BoatBayCoreModule } from 'app/core/core.module';
import { BoatBayAppRoutingModule } from './app-routing.module';
import { BoatBayHomeModule } from './home/home.module';
import { BoatBayEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { AngularMaterialModule } from 'app/angular-material.module';
import { environment } from 'app/environments/environment';
import { MatMenuModule } from '@angular/material/menu';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { SpecTileComponent } from 'app/components/spec-tile/spec-tile.component';
import { ProductComponent } from 'app/components/product/product.component';
import { ReleaseSelectorComponent } from 'app/components/release-selector/release-selector.component';
import { MatDividerModule } from '@angular/material/divider';
import { SpecsFilterComponent } from 'app/components/specs-filter/specs-filter.component';

@NgModule({
  imports: [
    BrowserModule,
    BoatBaySharedModule,
    BoatBayCoreModule,
    BoatBayHomeModule,
    AngularMaterialModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    BoatBayEntityModule,
    BoatBayAppRoutingModule,
    MatMenuModule,
    BrowserAnimationsModule,
    MatDividerModule,
  ],
  providers: [...environment.providers],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
  exports: [],
})
export class BoatBayAppModule {}
