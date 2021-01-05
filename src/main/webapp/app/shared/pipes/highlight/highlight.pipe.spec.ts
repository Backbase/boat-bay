import { HighlightPipe } from './highlight.pipe';

describe('HighlightPipe', () => {
  const testedString = 'Original test string';
  let pipe: HighlightPipe;

  beforeEach(() => {
    pipe = new HighlightPipe();
  });

  it('should find full matching word', () => {
    expect(pipe.transform(testedString, 'Original')).toBe('<mark>Original</mark> test string');
  });

  it('should find partially matching word', () => {
    expect(pipe.transform(testedString, 'Origi')).toBe('<mark>Origi</mark>nal test string');
  });

  it('should be case insensitive', () => {
    expect(pipe.transform(testedString, 'TEST')).toBe('Original <mark>test</mark> string');
  });

  it('should not find not matching word', () => {
    expect(pipe.transform(testedString, 'other')).toBe(testedString);
  });

  it('should return initial text if no arguments were provided', () => {
    expect(pipe.transform(testedString, '')).toBe(testedString);
  });
});
