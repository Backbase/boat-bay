import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'linkReplacer',
})
export class LinkReplacerPipe implements PipeTransform {
  private readonly urlInMarkdownRegexp = new RegExp('\\[.*\\]\\((www|http:|https:)+[^\\s]+[\\w]*\\)', 'gi');

  transform(text: string): string {
    return text.replace(this.urlInMarkdownRegexp, '[Link]');
  }
}
