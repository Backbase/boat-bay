import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IUpload } from 'app/shared/model/upload.model';

type EntityResponseType = HttpResponse<IUpload>;
type EntityArrayResponseType = HttpResponse<IUpload[]>;

@Injectable({ providedIn: 'root' })
export class UploadService {
  public resourceUrl = SERVER_API_URL + 'api/uploads';

  constructor(protected http: HttpClient) {}

  create(upload: IUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upload);
    return this.http
      .post<IUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(upload: IUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upload);
    return this.http
      .put<IUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(upload: IUpload): IUpload {
    const copy: IUpload = Object.assign({}, upload, {
      createdOn: upload.createdOn && upload.createdOn.isValid() ? upload.createdOn.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((upload: IUpload) => {
        upload.createdOn = upload.createdOn ? moment(upload.createdOn) : undefined;
      });
    }
    return res;
  }
}
