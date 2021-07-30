import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IZallyConfig, ZallyConfig } from '../zally-config.model';

import { ZallyConfigService } from './zally-config.service';

describe('Service Tests', () => {
  describe('ZallyConfig Service', () => {
    let service: ZallyConfigService;
    let httpMock: HttpTestingController;
    let elemDefault: IZallyConfig;
    let expectedResult: IZallyConfig | IZallyConfig[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ZallyConfigService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        contents: 'AAAAAAA',
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

      it('should create a ZallyConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ZallyConfig()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ZallyConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            contents: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ZallyConfig', () => {
        const patchObject = Object.assign({}, new ZallyConfig());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ZallyConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            contents: 'BBBBBB',
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

      it('should delete a ZallyConfig', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addZallyConfigToCollectionIfMissing', () => {
        it('should add a ZallyConfig to an empty array', () => {
          const zallyConfig: IZallyConfig = { id: 123 };
          expectedResult = service.addZallyConfigToCollectionIfMissing([], zallyConfig);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(zallyConfig);
        });

        it('should not add a ZallyConfig to an array that contains it', () => {
          const zallyConfig: IZallyConfig = { id: 123 };
          const zallyConfigCollection: IZallyConfig[] = [
            {
              ...zallyConfig,
            },
            { id: 456 },
          ];
          expectedResult = service.addZallyConfigToCollectionIfMissing(zallyConfigCollection, zallyConfig);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ZallyConfig to an array that doesn't contain it", () => {
          const zallyConfig: IZallyConfig = { id: 123 };
          const zallyConfigCollection: IZallyConfig[] = [{ id: 456 }];
          expectedResult = service.addZallyConfigToCollectionIfMissing(zallyConfigCollection, zallyConfig);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(zallyConfig);
        });

        it('should add only unique ZallyConfig to an array', () => {
          const zallyConfigArray: IZallyConfig[] = [{ id: 123 }, { id: 456 }, { id: 3378 }];
          const zallyConfigCollection: IZallyConfig[] = [{ id: 123 }];
          expectedResult = service.addZallyConfigToCollectionIfMissing(zallyConfigCollection, ...zallyConfigArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const zallyConfig: IZallyConfig = { id: 123 };
          const zallyConfig2: IZallyConfig = { id: 456 };
          expectedResult = service.addZallyConfigToCollectionIfMissing([], zallyConfig, zallyConfig2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(zallyConfig);
          expect(expectedResult).toContain(zallyConfig2);
        });

        it('should accept null and undefined values', () => {
          const zallyConfig: IZallyConfig = { id: 123 };
          expectedResult = service.addZallyConfigToCollectionIfMissing([], null, zallyConfig, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(zallyConfig);
        });

        it('should return initial array if no ZallyConfig is added', () => {
          const zallyConfigCollection: IZallyConfig[] = [{ id: 123 }];
          expectedResult = service.addZallyConfigToCollectionIfMissing(zallyConfigCollection, undefined, null);
          expect(expectedResult).toEqual(zallyConfigCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
