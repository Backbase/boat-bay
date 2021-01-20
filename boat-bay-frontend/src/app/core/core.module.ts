import { LOCALE_ID, NgModule } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { AuthInterceptor } from "./interceptor/auth.interceptor";
import { AuthExpiredInterceptor } from "./interceptor/auth-expired.interceptor";

@NgModule({
  imports: [
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-' }),
  ],
  providers: [
    Title,
    CookieService,
    {
      provide: LOCALE_ID,
      useValue: 'en',
    },
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthExpiredInterceptor,
      multi: true,
    }
  ],
})
export class BoatBayCoreModule {}
