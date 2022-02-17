import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ClientIdService {

  public static readonly KEY_CLIENT_ID = "TH-KEY-CLIENT-ID"; // underscore not allowed by default in nginx
  public static readonly KEY_CLIENT_VERSION = "TH-KEY-CLIENT-VERSION"; // underscore not allowed by default in nginx

  private clientId: string | null = '';

  constructor() {

    // check if we have an ID already
    this.clientId = localStorage.getItem(ClientIdService.KEY_CLIENT_ID);

    if (!this.clientId) {
      try {
        var array = new Uint32Array(2);
        crypto.getRandomValues(array);
        var arrayString = '';
        array.forEach(value => arrayString += value);

        this.clientId = arrayString;

      } catch (e) {
        console.warn("Fallback from crypto to Math.random()");
        this.clientId = "" + Math.random();
      }
      console.log("Created a new client id: ", this.clientId)
    } else {
      console.log("Got an existing client id: ", this.clientId);
    }

    localStorage.setItem(ClientIdService.KEY_CLIENT_ID, this.clientId);
    console.log("Did create or restore a client id: ", this.clientId);
  }

  getClientId(): string {
    if (this.clientId) {
      return this.clientId;
    } else {
      return 'unknown';
    }
  }
}
