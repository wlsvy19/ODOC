//  타입 단언(type assertion): ts보다 개발자가 타입을 더 잘 알고 있음

var f;
f = 20;
f = 'f';

// as: b가 string으로 단언 
var b = f as string;

// DOM: 웹페이지(HTML) 태그정보 접근 하여 조작
// DOM API 조작할 때 단언 사용
// <div id="app">hi</div>

// as를 붙임으로써 div는 무조건 HTMLDivElement 라고 개발자가 단언 함 -> 조건체크 안해도 됨
var div = document.querySelector('div') as HTMLDivElement;

// if(div) { //div가 null일 수도 있으므로
//   div.innerText
// }

div.innerText