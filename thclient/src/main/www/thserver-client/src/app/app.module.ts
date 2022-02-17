import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {OverviewCurrentComponent} from './overview-current/overview-current.component';

import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MatCardModule} from "@angular/material/card";
import {BoilerChartComponent} from './boiler-chart/boiler-chart.component';

import {HighchartsChartModule} from 'highcharts-angular';
import {MatIconModule} from "@angular/material/icon";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MAT_DATE_LOCALE, MatNativeDateModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
import {MatButtonModule} from '@angular/material/button';
import {MatSliderModule} from "@angular/material/slider";
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {NgxMaterialTimepickerModule} from 'ngx-material-timepicker';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatGridListModule} from "@angular/material/grid-list";
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MyHttpInterceptor} from "./my-http-interceptor.service";
import {FlexLayoutModule} from '@angular/flex-layout';
import {MatSelectModule} from "@angular/material/select";
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatDividerModule} from "@angular/material/divider";
import {RouterModule, Routes} from "@angular/router";
import {AboutComponent} from './about/about.component';
import {OverviewCurrentGaugeComponent} from "./overview-current/overview-current-gauge.component";
import {HashLocationStrategy, LocationStrategy, registerLocaleData} from "@angular/common";
import localeDeCH from '@angular/common/locales/de-CH';


export const routes: Routes = [
  {path: 'dashboard', component: OverviewCurrentComponent},
  {path: 'insights', component: BoilerChartComponent},
  {path: 'about', component: AboutComponent},
  {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
  {path: '**', redirectTo: 'dashboard', pathMatch: 'full'}
];

registerLocaleData(localeDeCH);

@NgModule({
  declarations: [
    AppComponent,
    OverviewCurrentComponent,
    OverviewCurrentGaugeComponent,
    BoilerChartComponent,
    AboutComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule, // attention: two ways of doing forms: required by many other components, e.g. Slider, etc
    ReactiveFormsModule, // attention: two ways of doing forms: required by many other components, e.g. Slider, etc
    FlexLayoutModule,
    MatCardModule,
    HighchartsChartModule,
    MatIconModule,
    MatExpansionModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatButtonModule,
    MatSliderModule,
    MatSlideToggleModule,
    NgxMaterialTimepickerModule,
    NgxMaterialTimepickerModule.setLocale('de-CH'),
    MatProgressSpinnerModule,
    MatGridListModule,
    MatToolbarModule,
    MatSelectModule,
    MatSnackBarModule,
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the app is stable
      // or after x seconds (whichever comes first).
      // registrationStrategy: 'registerWhenStable:30000'
      registrationStrategy: 'registerImmediately'
    }),
    MatCheckboxModule,
    MatDividerModule,
  ],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'de-CH'},
    {provide: LOCALE_ID, useValue: 'de-CH'},
    {provide: HTTP_INTERCEPTORS, useClass: MyHttpInterceptor, multi: true},
    {provide: LocationStrategy, useClass: HashLocationStrategy}, // use pi11#dashboard instead of pi11/dashboard as the later won't be found by a server, if directly accessed
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}
