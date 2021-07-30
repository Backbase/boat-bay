import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpecType, SpecType } from '../spec-type.model';

import { SpecTypeService } from './spec-type.service';

describe('Service Tests', () => {
  describe('SpecType Service', () => {
    let service: SpecTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpecType;
    let expectedResult: ISpecType | ISpecType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SpecTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        matchSpEL: 'AAAAAAA',
        icon: 'AAAAAAA',
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

      it('should create a SpecType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SpecType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SpecType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            matchSpEL: 'BBBBBB',
            icon: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SpecType', () => {
        const patchObject = Object.assign(
          {
            icon: 'BBBBBB',
          },
          new SpecType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SpecType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            matchSpEL: 'BBBBBB',
            icon: 'BBBBBB',
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

      it('should delete a SpecType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSpecTypeToCollectionIfMissing', () => {
        it('should add a SpecType to an empty array', () => {
          const specType: ISpecType = { id: 123 };
          expectedResult = service.addSpecTypeToCollectionIfMissing([], specType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(specType);
        });

        it('should not add a SpecType to an array that contains it', () => {
          const specType: ISpecType = { id: 123 };
          const specTypeCollection: ISpecType[] = [
            {
              ...specType,
            },
            { id: 456 },
          ];
          expectedResult = service.addSpecTypeToCollectionIfMissing(specTypeCollection, specType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SpecType to an array that doesn't contain it", () => {
          const specType: ISpecType = { id: 123 };
          const specTypeCollection: ISpecType[] = [{ id: 456 }];
          expectedResult = service.addSpecTypeToCollectionIfMissing(specTypeCollection, specType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(specType);
        });

        it('should add only unique SpecType to an array', () => {
          const specTypeArray: ISpecType[] = [{ id: 123 }, { id: 456 }, { id: 40632 }];
          const specTypeCollection: ISpecType[] = [{ id: 123 }];
          expectedResult = service.addSpecTypeToCollectionIfMissing(specTypeCollection, ...specTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const specType: ISpecType = { id: 123 };
          const specType2: ISpecType = { id: 456 };
          expectedResult = service.addSpecTypeToCollectionIfMissing([], specType, specType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(specType);
          expect(expectedResult).toContain(specType2);
        });

        it('should accept null and undefined values', () => {
          const specType: ISpecType = { id: 123 };
          expectedResult = service.addSpecTypeToCollectionIfMissing([], null, specType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(specType);
        });

        it('should return initial array if no SpecType is added', () => {
          const specTypeCollection: ISpecType[] = [{ id: 123 }];
          expectedResult = service.addSpecTypeToCollectionIfMissing(specTypeCollection, undefined, null);
          expect(expectedResult).toEqual(specTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
