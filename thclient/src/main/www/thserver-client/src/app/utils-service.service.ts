import {Injectable} from '@angular/core';

export class Interval {
  constructor(public key: string, public name: string, public intervalInSeconds: number) {
  }

  static compare(a: Interval, b: Interval): number {
    if (!a || !b) {
      return 0;
    }

    if (a.intervalInSeconds === b.intervalInSeconds) {
      return 0;
    } else {
      return a.intervalInSeconds > b.intervalInSeconds ? 1 : -1;
    }
  }

  static sort(intervals: Interval[]) {
    intervals.sort((a, b) => Interval.compare(a, b));
  }

}

@Injectable({
  providedIn: 'root'
})
export class UtilsServiceService {

  constructor() {
  }

  private static readonly standardIntervals = UtilsServiceService.getStandardIntervalsImpl();

  public static getStandardIntervals(): Interval[] {
    return this.standardIntervals;
  }


  public getIntervalInSecondsForMaxDataPoints(maxDataPoints: number, start: Date, end: Date): Interval {
    let intervals = UtilsServiceService.getStandardIntervals();
    let defaultInterval = intervals[0]; // smallest

    if (!start || !end) {
      return defaultInterval;
    }

    let deltaInSeconds = Math.abs((end.getTime() - start.getTime()) / 1000);
    let desiredInterval = deltaInSeconds / maxDataPoints;

    for (let i = 0; i < intervals.length; i++) {
      let interval = intervals[i];
      if (interval.intervalInSeconds > desiredInterval) {
        return interval; // found a match
      }
    }

    return defaultInterval;
  }

  private static getStandardIntervalsImpl(): Interval[] {

    let intervals: Interval[] = [];

    let second = 1;
    let minute = second * 60;
    let hour = minute * 60;
    let day = hour * 24;
    let month = 30.436875 * day; // in average (special years, normal years, etc)

    // seconds
    intervals.push(new Interval("1s", "1 second", second));
    intervals.push(new Interval("5s", "5 seconds", 5 * second));
    intervals.push(new Interval("15s", "15 seconds", 15 * second));
    intervals.push(new Interval("30s", "30 seconds", 30 * second));

    // minutes
    intervals.push(new Interval("1m", "1 minute", minute));
    intervals.push(new Interval("5m", "5 minutes", 5 * minute));
    intervals.push(new Interval("15m", "15 minutes", 15 * minute));
    intervals.push(new Interval("30m", "30 minutes", 30 * minute));

    // hours
    intervals.push(new Interval("1h", "1 hour", hour));
    intervals.push(new Interval("4h", "4 hours", 4 * hour));
    intervals.push(new Interval("8h", "8 hours", 8 * hour));

    // days
    intervals.push(new Interval("1d", "1 day", day));
    intervals.push(new Interval("3d", "3 days", 3 * day));

    // weeks
    intervals.push(new Interval("1w", "1 week", 7 * day));
    intervals.push(new Interval("2w", "2 weeks", 14 * day));

    // month
    intervals.push(new Interval("1m", "1 month", month));
    intervals.push(new Interval("3m", "3 months", 3 * month));
    intervals.push(new Interval("6m", "6 months", 6 * month));

    // years
    intervals.push(new Interval("1y", "1 year", 12 * month));

    Interval.sort(intervals); // sort them

    return intervals;
  }

}


