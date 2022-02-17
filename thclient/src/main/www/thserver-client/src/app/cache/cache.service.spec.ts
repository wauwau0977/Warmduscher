import { CacheService } from "./cache.service";
import {TestBed} from "@angular/core/testing";

describe('CurrentDataService', () => {
  let service: CacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
