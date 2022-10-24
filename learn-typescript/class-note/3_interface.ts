// 인터페이스: 동일한 규칙으로 사용하자는 약속
interface User {
  age: number;
  name: string;
}

// 변수에 인터페이스 활용
var seho: User = {
  age: 33,
  name: '세호'
}

// 함수에 인터페이스 활용
function getUser(user: User) {
  console.log(user)
}

const capt = {
  name: 'leejp',
  age: 30
}
getUser(capt);

// 함수 스펙(구조)에 인터페이스 활용
interface SumFunction {
  (a: number, b: number): number;
}


var sum: SumFunction;

sum = function (a: number, b: number): number {
  return a + b;
}

// 인터페이스의 인덱싱
interface StringArray {
  [index: number]: string;
}

var arr1: StringArray = ['a', 'b', 'c'];
// arr1[0] = 10;

// 딕셔너리 패턴
interface StringRegexDictionary {
  [key: string]: RegExp;
}

var obj1: StringRegexDictionary = {
  sth: /abc/,
  cssFile: /\.css$/,
  jsFile: /\.js$/,
}

// 인터페이스 확장
interface Person {
  name: string;
  age: number;
}

// Person 상속 가능 - extends
interface Developer extends Person {
  language: string;
}

var capt2: Developer = {
  language: 'java',
  name: 'leejp',
  age: 30,
}