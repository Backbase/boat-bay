import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from "./layout/navbar/navbar.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { LoginModalComponent } from "./login/login.component";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { environment } from "../environments/environment";
import { BoatBayCoreModule } from "./core/core.module";
import { MatIconModule } from "@angular/material/icon";
import { MatExpansionModule } from "@angular/material/expansion";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatInputModule } from "@angular/material/input";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatSelectModule } from "@angular/material/select";
import { MatSliderModule } from "@angular/material/slider";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatDialogModule } from "@angular/material/dialog";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { MatButtonModule } from "@angular/material/button";
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { LayoutModule } from '@angular/cdk/layout';
import { PortalDashboardModule } from "./pages/portal-dashboard/portal-dashboard.module";
import { ProductDashboardModule } from "./pages/product-dashboard/product-dashboard.module";
import { LintReportModule } from "./pages/lint-report/lint-report.module";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginModalComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BoatBayCoreModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatExpansionModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatToolbarModule,
    MatSelectModule,
    MatSliderModule,
    MatFormFieldModule,
    MatDialogModule,
    MatSlideToggleModule,
    MatButtonModule,
    PortalDashboardModule,
    ProductDashboardModule,
    LintReportModule,
    MatGridListModule,
    MatCardModule,
    MatMenuModule,
    LayoutModule
  ],
  providers: [...environment.providers],
  exports: [
    NavbarComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
