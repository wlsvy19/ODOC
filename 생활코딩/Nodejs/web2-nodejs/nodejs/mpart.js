//모듈
//파일로 쪼개어 외부로 독립 시키기
var M = {
    v:'v',
    f:function(){
        console.log(this.v);
    }
}

module.exports = M; //mparts.js에서 M이 가리키는 객체를 모듈 밖에서 사용할 수 있도록 한다.