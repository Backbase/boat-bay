import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { BoatDashboardConfiguration } from './configuration';
import { HttpClient } from '@angular/common/http';


@NgModule({
  imports:      [],
  declarations: [],
  exports:      [],
  providers:    []
})
export class BoatDashboardApiModule {
    public static forRoot(configurationFactory: () => BoatDashboardConfiguration): ModuleWithProviders<BoatDashboardApiModule> {
        return {
            ngModule: BoatDashboardApiModule,
            providers: [ { provide: BoatDashboardConfiguration, useFactory: configurationFactory } ]
        };
    }

    constructor( @Optional() @SkipSelf() parentModule: BoatDashboardApiModule,
                 @Optional() http: HttpClient,

        ) {
        if (parentModule) {
            throw new Error('BoatDashboardApiModule is already loaded. Import in your base AppModule only.');
        }
        if (!http) {
            throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
            'See also https://github.com/angular/angular/issues/20575');
        }

    }
}
