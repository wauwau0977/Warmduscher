import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {ClientIdService} from "./client-id.service";
import {HeatingDataService} from "./heating-data.service";
import {environment} from "../environments/environment";

@Injectable()
export class MyHttpInterceptor implements HttpInterceptor {

  constructor(private clientIdService: ClientIdService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let clientId = this.clientIdService.getClientId();
    let clientVersion = environment.buildTimestampClient;

    const modifiedReq = req.clone({
      headers: req.headers
        .set(ClientIdService.KEY_CLIENT_ID, clientId)
        .set(ClientIdService.KEY_CLIENT_VERSION, clientVersion)
    });
    return next.handle(modifiedReq);
  }
}
