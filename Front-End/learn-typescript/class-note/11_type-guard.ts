// 타입 가드: 타입을 지켜줌

interface Developer {
    name: string;
    skill: string
}

interface Person {
    name: string;
    age: number;
}

// 유니온 타입 -> 공통된 속성(name)만 접근 가능
function introduce(): Developer | Person {
    return {name: 'leejp', age: 33, skill: 'Java'}
}

var leejp = introduce();
//  공통된 속성(name)만 접근 가능
console.log(leejp.skill);

if ((leejp as Developer).skill) {
    var skill = (leejp as Developer).skill;
    console.log(skill);
} else if ((leejp as Person).age) {
    var age = (leejp as Person).age;
    console.log(age);
}

//  타입 가드 정의 is: 타입 가드 함수 만들기 - 이 함수 타고 나면 인자가 Developer인지 확인 가능 함
function isDeveloper(target : Developer | Person): target is Developer {
    return (target as Developer).skill !== undefined;
}

if (isDeveloper(leejp)) {
  // Developer -> skill 있음
    console.log(leejp.skill);
} else {
  // Person: age 있음
    console.log(leejp.age);
}