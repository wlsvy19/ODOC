// 타입스크립트 기본타입 지정 방법

// 1. JS 문자열
let str1 = "hello";

// 1. TS 문자열 선언
let str2: string = "hello";

// 2. TS 숫자 선언
let num: number = 10;

// 3. TS 배열 선언
let arr: Array<number> = [1, 2, 3];
let items: number[] = [1, 2, 3]; // 배열 리터럴[] 사용
let heroes: Array<string> = ["capt", "abc", "hello"];

// 4. TS 튜플: 배열 인덱스의 타입도 정의 가능
let address1: [string, string] = ["gangnam", "anyang"];
let address2: [string, number] = ["gangnam", 100];


// 5. TS 객체
let obj: object = {

}
// 객체면 어떤 타입 와도 괜찮
let person1: object = {
  name: 'capt',
  age: 100
}

// 객체 구체적 정의 가능, 객체 속성의 타입까지 선언과 동시에 정의 가능
let person2: {name: string, age: number} = {
  name: 'leejp',
  age: 31
}

// 6. TS: 진위값
let show: boolean = true;