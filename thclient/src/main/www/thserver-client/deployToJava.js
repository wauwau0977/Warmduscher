const fsExtra = require('fs-extra')
const fs = require('fs');
var path = require("path");

let target = '../../../../../thserver/src/main/resources/static';

// clean target directory
console.log("try to delete target directory " + path.resolve(target));

if (fs.existsSync(target)) {
  fs.rmSync(target, {recursive: true});
} else {
  console.log("no need to delete the directory ")
}

// copy folder and all subfolders
console.log("Now copy files to target directory")
fsExtra.copySync('./dist/thserver-client/', target)
