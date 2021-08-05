import { TestBed } from '@angular/core/testing';

import { BoatHttpService } from './boat-http.service';

describe('BoatHttpService', () => {
  let service: BoatHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BoatHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
