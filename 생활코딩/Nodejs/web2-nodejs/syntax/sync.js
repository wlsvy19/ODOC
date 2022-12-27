var fs = require('fs'); //fs모듈 읽어들이기

//readFileSync
// console.log('A');
// var result = fs.readFileSync('sample.txt', 'utf8');  // 리턴값이 있다
// console.log(result);
// console.log('C');


console.log('A');
fs.readFile('sample.txt', 'utf8', function(err, result){
    console.log(result);
});


console.log('C');
