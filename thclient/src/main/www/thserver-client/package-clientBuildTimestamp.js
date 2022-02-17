var replace = require('replace-in-file');
const moment = require("moment-timezone");

var timeStamp = moment(new Date())
  .tz("Europe/Zurich")
  .format("v01-YYYYMMDD-HHmmss");


const options = {
  files: ['src/environments/environment.prod.ts', "src/environments/environment.ts"],
  from: /\"?buildTimestampClient\"?\s*?:\s*?(\"|\')(.*?)(\"|\')/g,
  to: '\"buildTimestampClient\": \"' + timeStamp + '\"',
  allowEmptyPaths: false,
};

try {
  let changedFiles = replace.sync(options);
  console.log('buildTimestampClient set: ' + timeStamp + " in files: " + changedFiles);
} catch (error) {
  console.error('Error occurred:', error);
}
