/**
 * * 🎯 학습 목표
 *
 * 1. `const`와 `let`의 근본적인 차이점(재할당 여부)을 이해하고, 상황에 맞는 변수 선언 키워드를 선택할 수 있다.
 * 2. `const`와 `let`의 핵심 동작 원리인 '블록 스코프({ })' 개념을 이해하고, 구식 `var`와 무엇이 다른지 설명할 수 있다.
 * 3. React 개발에서 왜 `const`를 기본으로 사용해야 하는지, '불변성(Immutability)'의 관점에서 이해한다.
 *
 * -----------------------------------------------------------------------------------------
 *
 * 🔑 핵심 개념
 *
 * 1. `const` (Constant, 상수): '한 번만 할당하는' 변수. 한 번 새긴 이름표.
 * - '상수'라는 이름처럼, 한 번 값을 할당하면 그 변수에 다른 값을 '재할당'하는 것이 불가능하다.
 * - 선언과 동시에 값을 할당해야만 한다. (`const name;` 과 같은 선언은 에러 발생)
 * - [매우 중요] `const`는 재할당이 불가능하다는 것이지, 할당된 값 자체가 '불변'이라는 뜻은 아니다.
 * 만약 `const` 변수에 객체나 배열을 할당하면, 그 객체의 속성이나 배열의 요소는 변경할 수 있다.
 * 예: `const user = { name: 'Kim' }; user.name = 'Park'; // 이 코드는 정상 작동한다.`
 * 변수 `user`가 가리키는 메모리 주소를 바꾸는 재할당만 금지될 뿐이다.
 *
 * 2. `let`: '여러 번 할당 가능한' 변수. 내용을 지우고 다시 쓸 수 있는 화이트보드.
 * - `let`으로 선언된 변수는 코드의 흐름에 따라 다른 값으로 여러 번 재할당할 수 있다.
 * - 전통적인 프로그래밍의 '변수' 개념과 가장 가깝다.
 * - 단, React에서는 컴포넌트의 상태(데이터)를 `let`으로 직접 바꾸지 않고, `useState`라는 별도의 기능을 사용한다.
 * 따라서 `let`은 루프(loop)의 카운터 변수처럼 제한적인 상황에서만 사용된다.
 *
 * 3. 블록 스코프 (Block Scope): `const`와 `let`의 활동 영역. `{ }`라는 울타리.
 * - `const`와 `let`으로 선언된 변수는 자신이 선언된 `{ }`(중괄호, 블록) 안에서만 접근하고 사용할 수 있다.
 * - `if`, `for`, `while` 문 등 `{ }`를 사용하는 모든 구문은 자신만의 '블록 스코프'를 형성한다.
 * - 이 규칙 덕분에, 블록 안에서 선언된 변수가 바깥 세상에 영향을 주거나, 바깥 세상의 변수와 충돌하는 것을 막아준다.
 * 코드가 독립적인 부품처럼 작동하게 하여 예측 가능성을 높이고 버그를 줄여주는 매우 중요한 특징이다.
 *
 * 4. React와 불변성 (Immutability)
 * - React는 '상태(state)가 변경되면 화면을 새로 그린다'는 원칙으로 동작한다.
 * - 이 때, React가 상태 변경을 감지하려면, 기존의 상태를 직접 수정하는 것이 아니라 '새로운 상태'로 교체해야 한다.
 * 이것을 '불변성을 지킨다'고 표현한다.
 * - `let`을 사용해 변수 값을 직접 바꾸면 React는 변화를 감지하지 못할 수 있다.
 * - 반면, `const`를 기본으로 사용하면 개발자가 실수로라도 변수를 직접 수정하려는 시도를 문법적으로 막을 수 있다.
 * 따라서 개발자는 자연스럽게 `useState`와 같은 React의 올바른 상태 업데이트 방식을 사용하게 되므로, 코드의 안정성이 크게 향상된다.
 *
 */

// 컴포넌트 함수의 이름은 Part0_Ch1_Variables로 한다
function Part0_Ch1_Variables() {
    // --- 0. TDZ(Temporal Dead Zone) ---
    // console.log(beforeInit); // ❌ ReferenceError: 초기화 전 접근 불가
    let beforeInit = "TDZ 이후에 접근 가능";

    // --- 1. const ---
    const name = "이진표";
    // name = "박해커"; // ❌ TypeError

    // 객체/배열의 내부 값은 변경 가능
    const user = { name: "Kim" };
    user.name = "Park"; // O
    // 불변성을 지키는 방식
    const newUser = { ...user, age: 30 };

    // --- 2. let ---
    let age = 20;
    age = 21; // O

    // --- 3. 블록 스코프 ---
    let blockValue = "블록 밖";
    if (true) {
        let blockValue = "블록 안";
        console.log("if 블록 안에서:", blockValue); // "블록 안"
    }
    console.log("if 블록 밖에서:", blockValue); // "블록 밖"

    // --- 4. React에서 let 사용 시 문제 ---
    let count = 0;
    function wrongIncrement() {
        count++;
        console.log("count:", count); // 증가하지만 화면은 안 바뀜
    }

    return (
        <div>
            <h2>Part 0, Chapter 1: const vs let</h2>
            <p>
                <b>const</b>로 선언한 이름: {name}
            </p>
            <p>
                <b>let</b>으로 선언하고 변경한 나이: {age}
            </p>
            <p>불변성 예시: {JSON.stringify(newUser)}</p>
            <p>TDZ 변수: {beforeInit}</p>
            <button onClick={wrongIncrement}>let으로 카운트</button>
            <p>버튼 클릭 후 화면 값은 변하지 않음 (콘솔 확인)</p>
        </div>
    );
}

export default Part0_Ch1_Variables;