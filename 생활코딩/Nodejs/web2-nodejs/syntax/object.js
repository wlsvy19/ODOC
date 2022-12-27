//배열 리터럴은 대괄호
var members = ['leejp', 'k8805', '277ju7634'];
//배열 요소를 가져오는것도 대괄호
console.log(members[2]);
var i = 0;
while(i < members.length){
    console.log('Array loop',members[i]);
    i = i + 1;
}

//객체 리터럴은 중괄호
var roles = {
'programer':'leejp',
'designer':'k8805',
'manager':'kebunge'
}
for(var name in roles){ //변수에는 객체의 key값이 들어온다
    console.log('object => ',name, '  ,   value => ', roles[name]);
}
//객체 요소를 가져오는건 점
//대괄호 사용시 문자처럼 취급
console.log(roles.designer);
console.log(roles['designer']);