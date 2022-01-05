//함수의 성격



// function a(){
//     console.log('A');
// }

var a = function(){ //js에선 함수가 값으로 사용될 수 있다.
    console.log('A');
}

//a(); //a가 담고 있는 값인 함수를 실행할 수 있다.


function slowfunc(callback){
    callback();
}

slowfunc(a);

