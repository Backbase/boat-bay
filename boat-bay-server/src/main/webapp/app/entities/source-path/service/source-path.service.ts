import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISourcePath, getSourcePathIdentifier } from '../source-path.model';

export type EntityResponseType = HttpResponse<ISourcePath>;
export type EntityArrayResponseType = HttpResponse<ISourcePath[]>;

@Injectable({ providedIn: 'root' })
export class SourcePathService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/source-paths');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sourcePath: ISourcePath): Observable<EntityResponseType> {
    return this.http.post<ISourcePath>(this.resourceUrl, sourcePath, { observe: 'response' });
  }

  update(sourcePath: ISourcePath): Observable<EntityResponseType> {
    return this.http.put<ISourcePath>(`${this.resourceUrl}/${getSourcePathIdentifier(sourcePath) as number}`, sourcePath, {
      observe: 'response',
    });
  }

  partialUpdate(sourcePath: ISourcePath): Observable<EntityResponseType> {
    return this.http.patch<ISourcePath>(`${this.resourceUrl}/${getSourcePathIdentifier(sourcePath) as number}`, sourcePath, {
      observe: 'response',
    });
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

  addSourcePathToCollectionIfMissing(
    sourcePathCollection: ISourcePath[],
    ...sourcePathsToCheck: (ISourcePath | null | undefined)[]
  ): ISourcePath[] {
    const sourcePaths: ISourcePath[] = sourcePathsToCheck.filter(isPresent);
    if (sourcePaths.length > 0) {
      const sourcePathCollectionIdentifiers = sourcePathCollection.map(sourcePathItem => getSourcePathIdentifier(sourcePathItem)!);
      const sourcePathsToAdd = sourcePaths.filter(sourcePathItem => {
        const sourcePathIdentifier = getSourcePathIdentifier(sourcePathItem);
        if (sourcePathIdentifier == null || sourcePathCollectionIdentifiers.includes(sourcePathIdentifier)) {
          return false;
        }
        sourcePathCollectionIdentifiers.push(sourcePathIdentifier);
        return true;
      });
      return [...sourcePathsToAdd, ...sourcePathCollection];
    }
    return sourcePathCollection;
  }
}
