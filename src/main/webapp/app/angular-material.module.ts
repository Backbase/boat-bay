import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';

const materialImports = [
  MatCardModule,
  MatIconModule,
  MatExpansionModule,
  MatProgressSpinnerModule,
  MatInputModule,
  MatToolbarModule,
  MatSelectModule,
];

@NgModule({
  imports: materialImports,
  exports: materialImports,
})
export class AngularMaterialModule {}
