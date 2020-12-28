import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SourceService } from 'app/entities/source/source.service';
import { ISource, Source } from 'app/shared/model/source.model';
import { SourceType } from 'app/shared/model/enumerations/source-type.model';

describe('Service Tests', () => {
  describe('Source Service', () => {
    let injector: TestBed;
    let service: SourceService;
    let httpMock: HttpTestingController;
    let elemDefault: ISource;
    let expectedResult: ISource | ISource[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SourceService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Source(
        0,
        'AAAAAAA',
        'AAAAAAA',
        SourceType.GIT,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Source', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Source()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Source', () => {
        const returnedFromService = Object.assign(
          {
            baseUrl: 'BBBBBB',
            name: 'BBBBBB',
            type: 'BBBBBB',
            active: true,
            path: 'BBBBBB',
            filter: 'BBBBBB',
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Source', () => {
        const returnedFromService = Object.assign(
          {
            baseUrl: 'BBBBBB',
            name: 'BBBBBB',
            type: 'BBBBBB',
            active: true,
            path: 'BBBBBB',
            filter: 'BBBBBB',
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
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

      it('should delete a Source', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
