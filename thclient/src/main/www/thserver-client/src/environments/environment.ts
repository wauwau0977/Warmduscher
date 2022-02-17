// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  serviceBaseURL: "http://localhost:49088/pi11",
  //serviceBaseURL: "https://www.mindalyze.com/pi11/",
  fullPageRefreshInSeconds: 10 * 60 * 5,
  "buildTimestampClient": "v01-20220217-111246"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
