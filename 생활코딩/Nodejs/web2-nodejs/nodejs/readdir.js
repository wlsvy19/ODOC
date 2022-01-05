//data폴더의 파일을 읽어온다.

var testFolder = '../data';
var fs = require('fs');

fs.readdir(testFolder, function(error, filelist){
    console.log(filelist);
})


