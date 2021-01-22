import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LegacyPortalView } from "../models/dashboard/v1";
import { BoatDashboard } from "../models/boat-dashboard";
import {
  BoatCapability,
  BoatLintReport,
  BoatLintRule,
  BoatPortal,
  BoatProduct,
  BoatService,
  BoatSpec, BoatTag
} from "../models";
import { BoatProductRelease } from "../models/boat-product-release";

@Injectable({providedIn: 'root'})
export class BoatDashboardService {
  public resourceUrl = '/api/boat';

  constructor(protected http: HttpClient) {
  }

  getLegacyPortalView(): Observable<LegacyPortalView> {
    return this.http.get<LegacyPortalView>(`${this.resourceUrl}/legacy-dashboard`);
  }

  getBoatPortalView(): Observable<BoatDashboard[]> {
    return this.http.get<BoatDashboard[]>(`${this.resourceUrl}/dashboard`);
  }

  getBoatProducts(portalKey: string, productKey: string): Observable<HttpResponse<BoatProduct>> {
    return this.http.get<BoatProduct>(`${this.resourceUrl}/dashboard/${portalKey}/${productKey}`, {observe: 'response'});
  }

  getPortals(): Observable<HttpResponse<BoatPortal[]>> {
    return this.http.get<HttpResponse<BoatPortal[]>>(`${this.resourceUrl}/portals`);
  }

  getBoatCapabilities(portalKey: string, productKey: string, page: number, size: number, sort: string, direction: string): Observable<HttpResponse<BoatCapability[]>> {
    if (sort === undefined) {
      sort = "name";
    }

    const params = new HttpParams()
      .set("page", page.toString())
      .set("size", size.toString())
      .set("sort", sort + "," + direction)

    return this.http
      .get<BoatCapability[]>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/capabilities`,
        {
          params: params,
          observe: 'response'
        })
  }

  getBoatServices(portalKey: string, productKey: string, page: number, size: number, sort: string, direction: string): Observable<HttpResponse<BoatService[]>> {
    if (sort === undefined) {
      sort = "name";
    }
    let params = new HttpParams().set("page", page.toString())
      .set("size", size.toString())
      .set("sort", sort + "," + direction)

    return this.http.get<BoatCapability[]>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/services`, {

      params: params,
      observe: 'response'
    });
  }

  getBoatSpecs(portalKey: string, productKey: string, page: number, size: number, sort: string, direction: string): Observable<HttpResponse<BoatSpec[]>> {
    if (sort === undefined) {
      sort = "name";
    }
    let params = new HttpParams().set("page", page.toString())
      .set("size", size.toString())
      .set("sort", sort + "," + direction)
    return this.http.get<BoatSpec[]>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/specs`, {
      params: params,
      observe: 'response'
    });
  }

  getLintReport(portalKey: string, productKey: string, id: number, refresh: boolean = false): Observable<HttpResponse<BoatLintReport>> {
    return this.http.get<BoatLintReport>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/specs/${id}/lint-report?refresh=${refresh}`, { observe: 'response' });
  }

  getTags(portalKey: string, productKey: string):Observable<HttpResponse<BoatTag[]>> {
    return this.http.get<BoatTag[]>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/tags`, { observe: 'response' });
  }


  getReleases(portalKey: string, productKey: string):Observable<HttpResponse<BoatProductRelease[]>> {
    return this.http.get<BoatProductRelease[]>(`${this.resourceUrl}/portals/${portalKey}/products/${productKey}/releases`, { observe: 'response' });
  }


  getPortalLintRules(portalKey: string): Observable<HttpResponse<BoatLintRule[]>> {

    return this.http.get<BoatLintRule[]>(`${this.resourceUrl}/portals/${portalKey}/lint-rules`, {observe: "response"});
  }

  postPortalLintRule(portalKey: string, lintRule: BoatLintRule): Observable<HttpResponse<void>> {
    return this.http.post<void>(`${this.resourceUrl}/portals/${portalKey}/lint-rules/${lintRule.id}`, lintRule, {observe: 'response'});
  }


}
