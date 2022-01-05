var name = 'k8805 ';

var letter = 'Lorem' + name +
 'ipsum dolor sit amet, consectetur adipisicing elit, \n\
 \n\
 sed do eiusmod ' + name + '  tempor incididunt ut labore ' + name + ' et dolore magna aliqua. \
 Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo \
  consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu \
  fugiat nulla pariatur. Excepteur sint occaecat ' + name + ' cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.';

console.log(letter);

//줄바꿈이 매우 불편하다.

var a = 1; //1은 숫자라는 데이터를 표현하는 리터럴(literal)
//리터럴은 정보를 표현하는 방법, 기호

var letter2 = `Lorem ${name}
ipsum dolor sit amet, consectetur adipisicing elit, 
sed do eiusmod ${name} tempor incididunt ut labore ${name} et dolore magna aliqua. 

 Ut enim ad minim veni
 
 
 
 am, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo 



 
  consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu 
  fugiat nulla pariatur. Excepteur sint occaecat ' + name + ' cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. ${1+2+3}`;

  console.log(letter2);




