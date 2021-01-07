import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ellipsis',
})
export class EllipsisPipe implements PipeTransform {
  transform(value: string, length: number): string {
    if (!value) {
      return '';
    } else {
      return value.length > length ? `${value.substr(0, length).trim()}...` : value;
    }
  }
}
