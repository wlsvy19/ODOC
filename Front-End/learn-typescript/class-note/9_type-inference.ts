// 타입 추론 기본 1
var a = 'abc';

function getB(b = 10) {
    var c = 'hi';
    return b + a;
}

10 + '10' // 1010 

// 타입 추론 기본 2
/*
interface Dropdown<T> {
  value: T; // 쓰는곳에서 value의 타입 추론 가능
  title: string;
}

var shoppingItem: Dropdown<string> = {
  value: 'abc', // 제네릭 T 에 string이 들어감
  title: 'hello'
}
 */

// 타입 추론 기본 3
interface Dropdown<T> {
    value: T; // 쓰는곳에서 value의 타입 추론 가능
    title: string;
}

interface DetailedDropdown<K> extends Dropdown<K> {
    description: string;
    tag: K;
}

var detailedItem: DetailedDropdown<string> = {
    title: 'abc',
    description: 'def',
    value: 'g',
    tag: 'h'
}

// Best Common Type 추론 방식
var arr = [1, 2, 'a', true];