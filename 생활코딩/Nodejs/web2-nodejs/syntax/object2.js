//JavaScript-객체-값 으로서 함수

// 데이터를 사용하기 좋게 정리 : array, object

//if문은 값이 될 수 없네?
// var i = if(true){
//     console.log(1);
// }

// //while문도 값이 될 수 없네?
// var j = while(true){
//     console.log(1);
// }

//js의 함수는 처리해야할 구문이면서 값이기도 한다.(변수에 넣을 수 있음)
var k = function f1() {
    console.log(1 + 1);
    console.log(1 + 2);
}
console.log(k);
k();

//배열의 원소로서 함수를 사용할 수 있다.
var a = [k];
a[0](); 

//객체의 요소로서 함수를 사용할 수 있다.
var o = {
    func:k
}
o.func();