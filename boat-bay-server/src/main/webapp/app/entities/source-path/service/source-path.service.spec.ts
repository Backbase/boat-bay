import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISourcePath, SourcePath } from '../source-path.model';

import { SourcePathService } from './source-path.service';

describe('Service Tests', () => {
  describe('SourcePath Service', () => {
    let service: SourcePathService;
    let httpMock: HttpTestingController;
    let elemDefault: ISourcePath;
    let expectedResult: ISourcePath | ISourcePath[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SourcePathService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SourcePath', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SourcePath()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SourcePath', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SourcePath', () => {
        const patchObject = Object.assign({}, new SourcePath());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SourcePath', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SourcePath', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSourcePathToCollectionIfMissing', () => {
        it('should add a SourcePath to an empty array', () => {
          const sourcePath: ISourcePath = { id: 123 };
          expectedResult = service.addSourcePathToCollectionIfMissing([], sourcePath);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sourcePath);
        });

        it('should not add a SourcePath to an array that contains it', () => {
          const sourcePath: ISourcePath = { id: 123 };
          const sourcePathCollection: ISourcePath[] = [
            {
              ...sourcePath,
            },
            { id: 456 },
          ];
          expectedResult = service.addSourcePathToCollectionIfMissing(sourcePathCollection, sourcePath);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SourcePath to an array that doesn't contain it", () => {
          const sourcePath: ISourcePath = { id: 123 };
          const sourcePathCollection: ISourcePath[] = [{ id: 456 }];
          expectedResult = service.addSourcePathToCollectionIfMissing(sourcePathCollection, sourcePath);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sourcePath);
        });

        it('should add only unique SourcePath to an array', () => {
          const sourcePathArray: ISourcePath[] = [{ id: 123 }, { id: 456 }, { id: 38358 }];
          const sourcePathCollection: ISourcePath[] = [{ id: 123 }];
          expectedResult = service.addSourcePathToCollectionIfMissing(sourcePathCollection, ...sourcePathArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sourcePath: ISourcePath = { id: 123 };
          const sourcePath2: ISourcePath = { id: 456 };
          expectedResult = service.addSourcePathToCollectionIfMissing([], sourcePath, sourcePath2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sourcePath);
          expect(expectedResult).toContain(sourcePath2);
        });

        it('should accept null and undefined values', () => {
          const sourcePath: ISourcePath = { id: 123 };
          expectedResult = service.addSourcePathToCollectionIfMissing([], null, sourcePath, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sourcePath);
        });

        it('should return initial array if no SourcePath is added', () => {
          const sourcePathCollection: ISourcePath[] = [{ id: 123 }];
          expectedResult = service.addSourcePathToCollectionIfMissing(sourcePathCollection, undefined, null);
          expect(expectedResult).toEqual(sourcePathCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
