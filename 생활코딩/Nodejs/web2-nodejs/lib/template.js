//리팩토링
module.exports = {
    //객체의 값들을 프로퍼티 라고 한다.
    HTML: function (title, list, body, control) { // 템플릿 리터럴을 사용하여 줄바꿈을 쉽게 한다.
      return ` 
      <!doctype html>
      <html>
      <head>
         <title>WEB1 - ${title}</title>
         <meta charset="utf-8">
      </head>
       <body>
         <h1><a href="/">WEB1</a></h1>
         ${list}
         ${control}
         ${body}
       </body>
      </html>
      `;
    }, list: function (filelist) {
      var list = '<ul>';
      var i = 0;
      while (i < filelist.length) {
        list = list + `<li><a href="/?id=${filelist[i]}">${filelist[i]}</a></li>`;
        i = i + 1;
      }
      list = list + '</ul>';
      return list;
    }
  }

  