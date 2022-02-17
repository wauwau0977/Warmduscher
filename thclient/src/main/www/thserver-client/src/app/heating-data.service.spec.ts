import { TestBed } from '@angular/core/testing';

import { HeatingDataService } from './heating-data.service';

describe('CurrentDataService', () => {
  let service: HeatingDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeatingDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

});
