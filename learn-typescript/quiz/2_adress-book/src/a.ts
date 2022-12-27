// * 타입추론 o
function fetchItems1(): string[] {
  let items: string[] = ["a", "b", "c"];
  return items;
}

// * 비동기, Promise 자체가 Generic을 받도록 구현 되어있음
// * 타입추론 x
function fetchItems2(): Promise<string[]> {
  let items: string[] = ["a", "b", "c"];
  return new Promise(function (resolve) {
    resolve(items);
  });
}

fetchItems1();
fetchItems2();
