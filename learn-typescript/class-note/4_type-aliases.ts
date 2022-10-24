// 타입 별칭

// 인터페이스와 타입 큰 차이점: 인터페이스는 extends로 확장 가능(상속), 타입은 불가능

interface Person1 {
  name: string;
  age: number;
}

// type Person1 = {
//   name: string;
//   age: number;
// }

// Person1에 마우스 대면 보여지는게 다름
var leejp: Person1 = {
  name: 'jp',
  age: 31
}

type MyString = string;
var str: MyString = 'hello';

type Todo = {id:string, title: string, done: boolean};

// function getTodo(todo: { id: string, title: string, done: boolean }) {
//   id: 'jp';
//   title: 'title';
//   done: true;}
// }

// 타입 별칭 쓰면 코드 줄어짐
function getTodo(todo: Todo) {
  id: 'jp';
  title: 'title';
  done: true;
}

// 타입 별칭: 참고 느낌
// 인터페이스: 약속 느낌
