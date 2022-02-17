import { Component, OnInit } from '@angular/core';
import {environment} from "../../environments/environment";
import {HeatingDataService} from "../heating-data.service";

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.sass']
})
export class AboutComponent implements OnInit {

  buildTimestampClient = environment.buildTimestampClient;
  buildTimestampServer = "";


  constructor(private heatingDataService: HeatingDataService) { }

  ngOnInit(): void {
    this.getBuildTimestampServer();
  }

  getBuildTimestampServer() {
    // @ts-ignore
    this.heatingDataService.getServerInfo().subscribe(info => this.buildTimestampServer = info.buildTimestampServer);
    return this.buildTimestampServer;
  }
}
