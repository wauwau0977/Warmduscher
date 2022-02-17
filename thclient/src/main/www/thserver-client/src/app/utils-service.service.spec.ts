import {TestBed} from '@angular/core/testing';

import {Interval, UtilsServiceService} from './utils-service.service';

describe('UtilsServiceService', () => {
  let service: UtilsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UtilsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('find interval: 1 week 10 data points --> 1d', () => {
    let interval: Interval = service.getIntervalInSecondsForMaxDataPoints(10, new Date(2021, 1, 1), new Date(2021, 1, 8)); // 1 week 10 data points
    console.log(interval);
    expect(interval).not.toBeNull();
    expect(interval.key).toEqual("1d");
  });

  it('find interval: 1 week 200 data points --> 1h', () => {
    let interval: Interval = service.getIntervalInSecondsForMaxDataPoints(200, new Date(2021, 1, 1), new Date(2021, 1, 8));
    console.log(interval);
    expect(interval).not.toBeNull();
    expect(interval.key).toEqual("1h");
  });

  it('find interval: 1 week 165 data points --> 4h', () => {
    let interval: Interval = service.getIntervalInSecondsForMaxDataPoints(165, new Date(2021, 1, 1), new Date(2021, 1, 8)); // 1 week 10 data points
    console.log(interval);
    expect(interval).not.toBeNull();
    expect(interval.key).toEqual("4h");
  });

  it('find interval: 1 year 366 data points --> 1d', () => {
    let interval: Interval = service.getIntervalInSecondsForMaxDataPoints(366, new Date(2021, 1, 1), new Date(2022, 1, 1)); // 1 week 10 data points
    console.log(interval);
    expect(interval).not.toBeNull();
    expect(interval.key).toEqual("1d");
  });

  it('find interval: 1 year 360 data points --> 3d', () => {
    let interval: Interval = service.getIntervalInSecondsForMaxDataPoints(360, new Date(2021, 1, 1), new Date(2022, 1, 1)); // 1 week 10 data points
    console.log(interval);
    expect(interval).not.toBeNull();
    expect(interval.key).toEqual("3d");
  });


});
