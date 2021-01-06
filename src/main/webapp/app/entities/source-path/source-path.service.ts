import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISourcePath } from 'app/shared/model/source-path.model';

type EntityResponseType = HttpResponse<ISourcePath>;
type EntityArrayResponseType = HttpResponse<ISourcePath[]>;

@Injectable({ providedIn: 'root' })
export class SourcePathService {
  public resourceUrl = SERVER_API_URL + 'api/source-paths';

  constructor(protected http: HttpClient) {}

  create(sourcePath: ISourcePath): Observable<EntityResponseType> {
    return this.http.post<ISourcePath>(this.resourceUrl, sourcePath, { observe: 'response' });
  }

  update(sourcePath: ISourcePath): Observable<EntityResponseType> {
    return this.http.put<ISourcePath>(this.resourceUrl, sourcePath, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISourcePath>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISourcePath[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
