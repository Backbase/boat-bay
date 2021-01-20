import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
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
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SourceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Source(
        0,
        'AAAAAAA',
        SourceType.GIT,
        'AAAAAAA',
        false,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        false
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Source', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.create(new Source()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Source', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            baseUrl: 'BBBBBB',
            active: true,
            filterArtifactsName: 'BBBBBB',
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            runOnStartup: true,
            specFilterSpEL: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
            specKeySpEL: 'BBBBBB',
            versionSpEL: 'BBBBBB',
            productReleaseNameSpEL: 'BBBBBB',
            productReleaseVersionSpEL: 'BBBBBB',
            productReleaseKeySpEL: 'BBBBBB',
            itemLimit: 1,
            overwriteChanges: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Source', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            baseUrl: 'BBBBBB',
            active: true,
            filterArtifactsName: 'BBBBBB',
            filterArtifactsCreatedSince: currentDate.format(DATE_FORMAT),
            username: 'BBBBBB',
            password: 'BBBBBB',
            cronExpression: 'BBBBBB',
            runOnStartup: true,
            specFilterSpEL: 'BBBBBB',
            capabilityKeySpEL: 'BBBBBB',
            capabilityNameSpEL: 'BBBBBB',
            serviceKeySpEL: 'BBBBBB',
            serviceNameSpEL: 'BBBBBB',
            specKeySpEL: 'BBBBBB',
            versionSpEL: 'BBBBBB',
            productReleaseNameSpEL: 'BBBBBB',
            productReleaseVersionSpEL: 'BBBBBB',
            productReleaseKeySpEL: 'BBBBBB',
            itemLimit: 1,
            overwriteChanges: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            filterArtifactsCreatedSince: currentDate,
          },
          returnedFromService
        );

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