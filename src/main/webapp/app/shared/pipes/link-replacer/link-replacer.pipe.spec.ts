import { LinkReplacerPipe } from './link-replacer.pipe';

describe('LinkReplacerPipe', () => {
  it('should change the actual link with placeholder', () => {
    const textWithLink =
      '# Action Client API documentation. . Overall capability description can be found [here](https://community.backbase.com/documentation/DBS/latest/actions_reference#actions_services) text after link';
    const textWithPlaceholderInsteadOfLink =
      '# Action Client API documentation. . Overall capability description can be found [Link] text after link';
    const pipe = new LinkReplacerPipe();
    expect(pipe.transform(textWithLink)).toBe(textWithPlaceholderInsteadOfLink);
  });
});
