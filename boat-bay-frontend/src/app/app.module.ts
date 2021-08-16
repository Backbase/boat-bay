import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from "./layout/navbar/navbar.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { LoginModalComponent } from "./login/login.component";
import { HttpClientModule } from "@angular/common/http";
import { environment } from "../environments/environment";
import { BoatBayCoreModule } from "./core/core.module";
import { MatIconModule } from "@angular/material/icon";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { RouterModule } from "@angular/router";
import { PortalDashboardComponent } from "./pages/portal-dashboard/portal-dashboard.component";
import { PortalProductsComponent } from './components/portal-products/portal-products.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginModalComponent,
    PortalDashboardComponent,
    PortalProductsComponent,
  ],
  imports: [
    RouterModule,
    BrowserModule,
    HttpClientModule,
    BoatBayCoreModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatToolbarModule,
    MatCardModule,
    MatMenuModule,
    MatSlideToggleModule,
    MatDialogModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatButtonModule,
  ],
  providers: [...environment.providers],
    exports: [
        NavbarComponent,
    ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
