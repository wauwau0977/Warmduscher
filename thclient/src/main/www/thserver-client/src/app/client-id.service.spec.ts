import {TestBed} from '@angular/core/testing';

import {ClientIdService} from './client-id.service';

describe('ClientIdService', () => {
  let service: ClientIdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientIdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it("test create client id", () => {
    expect(service.getClientId()).not.toBeNull();
  });

  it("test create client id", () => {
    let serviceID1 = service.getClientId();
    let serviceID2 = service.getClientId();
    expect(serviceID1).toEqual(serviceID2);
  });


});
