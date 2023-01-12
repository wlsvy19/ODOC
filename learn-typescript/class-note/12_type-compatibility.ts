// 타입 호환

// 인터페이스 선언 - Developer가 더 큼
interface Developer {
    name: string;
    skill: string;
}

interface Person {
  name: string;
  skill: string;
}

var developer: Developer;
var person: Person;

developer = person;
// person = developer;


// 클래스도 마찬 가지
class PersonClass {
  name: string;
  skill:string;
}

developer = new PersonClass();

// 함수와 제네릭 타입 호환

// 함수

var add = function (a: number) {
  // 로직 실행..
}

// sum > add
var sum = function (a: number, b: number){
  // 로직 실행..

}


add = sum; // add는 인자 1개밖에 못받음 불가능

sum = add; // sum은 인자 2개 받을 수 있어서 가능


// 제네릭
interface Empty<T> {

}
var empty1: Empty<string> = {};
var empty2: Empty<number> = {};

empty1 = empty2;
empty2 = empty1;

interface NotEmpty<T> {
  data: <T>;
}

var notempty1: NotEmpty<string>;
var notempty2: NotEmpty<number>;

notempty2 = notempty1;
notempty1 = notempty2;

