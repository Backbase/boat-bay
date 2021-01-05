import { NgModule } from '@angular/core';
import { BoatBaySharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { EllipsisPipe } from './pipes/ellipsis/ellipsis.pipe';
import { HighlightPipe } from './pipes/highlight/highlight.pipe';
import { LinkReplacerPipe } from './pipes/link-replacer/link-replacer.pipe';
import { EmptyStateComponent } from 'app/shared/components/empty-state/empty-state.component';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  imports: [BoatBaySharedLibsModule, MatCardModule],
  declarations: [
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    EllipsisPipe,
    HighlightPipe,
    LinkReplacerPipe,
    EmptyStateComponent,
  ],
  entryComponents: [LoginModalComponent],
  exports: [
    BoatBaySharedLibsModule,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    EllipsisPipe,
    HighlightPipe,
    LinkReplacerPipe,
    EmptyStateComponent,
  ],
})
export class BoatBaySharedModule {}
