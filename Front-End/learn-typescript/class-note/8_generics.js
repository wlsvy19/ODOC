// function logText(text) {
//   console.log(text);
//   return text;
// }
// logText(10);
// logText('leejp');
// logText(true);
// generic: 호출 하는 시점에서 타입을 정함
function logGeneric(text) {
    console.log(text);
    return text;
}
logGeneric('hi');
function logText(text) {
    console.log(text);
    // 숫자 거꾸로 하는 로직 -> 문자만 받을 수 있음
    text.split('').reverse().join('');
    return text;
}
logText('leejp');
function logNumber(text) {
    console.log(text);
    // 숫자 조작 로직
    text.toString;
    return text;
}
logNumber(10);
function logBoolean(text) {
    console.log(text);
    // 부울린 조작 로직
    text.toString;
    return text;
}
logBoolean(true);
// 타입만 다르게 받는다고 함수를 이렇게 선언해야하나??
// 유니온 타입 사용할 수 있지만, 문자열 관련 로직 실행시 문자열관련 API만
// 사용하고 싶은데 숫자 관련 API도 계속 뜸
function logUnion(text) {
    console.log(text);
    return text;
}
// 제네릭 사용시 이점
// <T>: 어떤 타입으로 받을거야?
function logGenericBenefit(text) {
    console.log(text);
    return text;
}
// 문자열 관련 API 사용 가능
var str = logGenericBenefit('abc');
//str.
// boolean 관련 API 사용 가능
var login = logGenericBenefit(true);
var obj1 = { value: 10, selected: false }; // value를 number로 쓰고싶은데~?
var obj2 = { value: 'abc', selected: false };
/* 제네릭의 타입 제한1 */
// 문자열 길이 출력 함수 -> 길이 출력 하고싶어서 배열[] 붙임
function logTextLength1(text) {
    // text가 배열이라서 length, foreach 사용 가능
    text.length;
    text.forEach(function (text) {
        console.log(text);
    });
    return text;
}
logTextLength1(['hi', 'hello']);
// length는 어차피 number가 들어있다
function logTextLength2(text) {
    text.length;
    return text;
}
logTextLength2("abc"); // 문자는 기본적으로 length 있음
// logTextLength2(10.length);
// logTextLength2(10); // 숫자에는 length속성 없음
logTextLength2({ length: 10 });
// ShoppingItem에 있는 옵션중 한가지가 제네릭이 됨(제네릭의 타입을 줄임)
function getShoppingItemOption(itemOption) {
    return itemOption;
}
// <T>라서 다받을 수 있음->keyof사용시 에러
//getShoppingItemOption(10);
// getShoppingItemOption('abc');
getShoppingItemOption("name");
getShoppingItemOption("price");
getShoppingItemOption("stock");
