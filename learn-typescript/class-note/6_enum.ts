// enum: 드롭다운 만들 때 유용

// 숫자형 enum
// enum Shoes {
//   Nike = 10,
//   Adidas // 1씩증가
// }

// var myShoes = Shoes.Nike;
// console.log(myShoes); // 결과 0: 별도값 지정 안하면 숫자형enum으로 취급

// 문자형 enum
enum Shoes {
  Nike = '나이키',
  Adidas = '아디다스'
}

var myShoes = Shoes.Nike;
console.log(myShoes);

enum Answer {
  Yes = 'Y',
  No = 'N'
}

function askQuestion(answer: Answer) {
  if (answer === Answer.Yes) {
      console.log('정답입니다.');
    } 
  if (answer === Answer.No) {
      console.log('오답입니다.');
    }
}

askQuestion(Answer.Yes);
// askQuestion('y');
// askQuestion('Yes')