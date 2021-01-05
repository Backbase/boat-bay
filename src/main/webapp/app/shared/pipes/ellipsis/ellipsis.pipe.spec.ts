import { EllipsisPipe } from './ellipsis.pipe';

describe('EllipsisPipe', () => {
  let pipe: EllipsisPipe;
  const emptyText = '';
  const titleTextMock = 'Flow Document Storage API Specification';
  const titleTextTruncatedMock = 'Flow Document Storage API Specifica...';
  const shortDescriptionTextMock = 'Lorem Ipsum is simply dummy text';
  const descriptionTextMock =
    'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.';
  const descriptionTextTruncatedMock =
    'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since...';

  beforeEach(() => {
    pipe = new EllipsisPipe();
  });

  it('should render empty string', () => {
    expect(pipe.transform(emptyText, 10)).toBe('');
  });

  it('when provided decription text is less than 140 characters then it should return as it is', () => {
    expect(pipe.transform(shortDescriptionTextMock, 140)).toBe(shortDescriptionTextMock);
  });

  it('when provided decription text is more than 140 characters then it should return the truncated text', () => {
    expect(pipe.transform(descriptionTextMock, 140)).toBe(descriptionTextTruncatedMock);
  });

  it('when provided title text is more than 35 characters then it should return the truncated text', () => {
    expect(pipe.transform(titleTextMock, 35)).toBe(titleTextTruncatedMock);
  });
});
