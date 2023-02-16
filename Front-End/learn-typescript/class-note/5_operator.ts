// function logMessage(value: any) {
//   console.log(value);
// }

// logMessage('message...');
// logMessage(100);


// 유니온 타입: 1가지 이상 타입 지정, 파이프연산자 사용 |
function logMessage(value: string | number) {
  // 타입가드: 특정 타입으로 타입의 범위를 좁혀 나가는(필터링) 하는 과정
  if (typeof value === 'number') {
    value.toLocaleString(); // number API 사용 가능
  }
  if (typeof value === 'string') {
    value.charAt(1); // string API 사용 가능
  }
  throw new TypeError('string 또는 number만 가능 합니다.');
}

var leejp2: string | number | boolean;

// 유니온 타입 특징
interface Developer1 {
  name: string;
  skill: string;
}
interface Person1 {
  name: string;
  age: number;
}

// 함수 인자로 유니온 타입 받음
function askSomeone(someone: Developer1 | Person1) {
  // 공통 속성만 접근, Developer도 되야하고 Person도 되야 하기 때문에, 보장된 속성만 접근
  // someone.name;
  // someone.skill; 
  // someone.number;
}
// 유니온: 인자 넘길때 선택지 생김, 실무에서 많이 쓰임
askSomeone({ name: '디벨로퍼', skill: '웹 개발' });
askSomeone({ name: '캡틴', age: 100 });


// 인터섹션 타입 - &
// var leejp3: string & number & boolean;

function askSomeone1(someone: Developer1 & Person1) {
  // 공통 속성만 접근, Developer도 되야하고 Person도 되야 하기 때문에, 보장된 속성만 접근
  someone.name;
  someone.skill;
  someone.age;
}

// 인터섹션: 선택지 없고 무조건 다 넣어야 함
askSomeone1({ name: '디벨로퍼', skill: '웹 개발', age:30});
askSomeone1({ name: '캡틴', age: 100, skill:'자바' });