// JS 클래스: ES2015 (ES6) 추가
// 클래스의 목적: 인스턴스(객체) 생성

// 생성자 패턴 이용한 문법만 달라진거임. 빌드 후 바벨에서 보면 생성자 함수로 클래스 만듬
// 자바 개발자등이 사용하기 편하라고 만듬
function Person(name, age) {
  this.name = name;
  this.age = age;
}
var capt = new Person('캡틴', 100);
console.log(capt);
/* 위아래 같음 */

class Person {
  // 클래스 로직...
  // 1. 생성자로 초기화
  constructor(name, age) {
    console.log('생성되었습니다.');
    this.name = name;
    this.age = age
  }
}
var seho = new Person('세호', 30);
console.log(seho);