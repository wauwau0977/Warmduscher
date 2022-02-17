import {Component} from '@angular/core';
import {interval} from "rxjs";
import {environment} from "../environments/environment";
import {HeatingDataService} from "./heating-data.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {

  title = 'thserver-client';


  constructor() {
    console.log("Setup the page refresh mechanism all " + environment.fullPageRefreshInSeconds + " seconds.");

    // figure out a client id

  }

  myFullPageRefresh(): void {
    window.location.reload();
  }

  subscribe = interval(environment.fullPageRefreshInSeconds * 1000).subscribe(
    val => {
      console.log("Execute full page refresh... ")
      this.myFullPageRefresh();
    }
  );


}
