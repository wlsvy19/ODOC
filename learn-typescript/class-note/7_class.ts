class Person1 {
  // 접근제어자 사용가능
  private name: string; 
  public age: number;
  readonly log: string; // readonly: 읽기만 가능, set 불가

  constructor(name: string, age: number) {
    console.log('생성되었습니다.');
    this.name = name;
    this.age = age
  }
}