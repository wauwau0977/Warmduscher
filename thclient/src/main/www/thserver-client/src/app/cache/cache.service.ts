import {Injectable} from "@angular/core";
import {Observable, of, shareReplay} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CacheService {

  private cache = {};

  public get(cacheKey: string, loader: () => Observable<any>, evict: boolean, doNotCache?: boolean): Observable<any> {

    if(doNotCache) {
      return loader();
    }

    // requested to clear the cache
    if (evict) {
      this.cache[cacheKey] = null;
    }

    // cache is empty, load it
    if (this.cache[cacheKey] == null) {
      // wrap the inner cache into a shared replay object and hand out the proxy
      this.cache[cacheKey] = loader().pipe(shareReplay(1));
    }

    // now return the result
    return this.cache[cacheKey];
  }


}
