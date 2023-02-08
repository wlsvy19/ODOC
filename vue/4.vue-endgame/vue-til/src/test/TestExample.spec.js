import { sum } from './math';

// sum(10, 20); // 30

// describe: test에 사용하는 API로 하나의 테스트 케이스 묶음
describe('math.js', () => {
  // test: 하나의 테스트 케이스를 검증하는 API (테스트 명, 콜백 함수)
  test('10 + 20 = 30 일까?', () => {
    const result = sum(10, 20);

    // result의 결과가 30 이냐
    expect(result).not.toBe(40);
    expect(result).toBe(30);
  });
});
