//모듈
//파일로 쪼개어 외부로 독립 시키기
// var M = {
//     v:'v',
//     f:function(){
//         console.log(this.v);
//     }
// }

// ./ 는 현재 디렉토리
//모듈을 읽어들일때 require사용
var part = require('./mpart.js');

console.log(part);
part.f();