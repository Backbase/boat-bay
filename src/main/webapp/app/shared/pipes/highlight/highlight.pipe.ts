import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'highlight',
})
export class HighlightPipe implements PipeTransform {
  transform(value: string, args: string | null): string {
    if (!args) {
      return value;
    }
    const re = new RegExp(args, 'gi');

    return value.replace(re, '<mark>$&</mark>');
  }
}
