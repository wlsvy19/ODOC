// 1. 함수 파라미터 타입 정의하는 방식
function sum3(a: number, b: number) {
  return a + b;
}
sum3(10, 20);

// 2. 함수 반환값에 타입을 정의하는 방식
function sum4(): number {
  return 10;
}
sum4();

// 3. 함수에 타입을 정의하는 방식
function sum6(a: number, b: number): number {
  return a + b;
}

sum6(10, 20);
//sum6(10, 20, 30, 40); // 함수의 형태도 유지해주며 인자 넘겨 줘야함

// 4. 함수의 옵셔널 파라미터 - ? 붙이면 선택적 사용 가능
function log(a?: string, b?: string){

}
log('hello');
log('hello ts', 'abc');
