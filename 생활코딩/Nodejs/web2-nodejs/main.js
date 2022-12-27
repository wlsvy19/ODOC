// node.js의 모듈 선언
var http = require('http');
var fs = require('fs'); // file system
var url = require('url');
var qs = require('querystring');
var template = require('./lib/template.js');
var path = require('path');//비번 안나오게 parse 사용 하기 위한 모듈
var sanitizeHtml = require('sanitize-html'); //script태그를 심어놓은 공격 같은거 방지

// //리팩토링 후, 모듈로 뺀다. ↑
// var template = {
//   //객체의 값들을 프로퍼티 라고 한다.
//   HTML: function (title, list, body, control) { // 템플릿 리터럴을 사용하여 줄바꿈을 쉽게 한다.
//     return ` 
//     <!doctype html>
//     <html>
//     <head>
//        <title>WEB1 - ${title}</title>
//        <meta charset="utf-8">
//     </head>
//      <body>
//        <h1><a href="/">WEB1</a></h1>
//        ${list}
//        ${control}
//        ${body}
//      </body>
//     </html>
//     `;
//   }, list: function (filelist) {
//     var list = '<ul>';
//     var i = 0;
//     while (i < filelist.length) {
//       list = list + `<li><a href="/?id=${filelist[i]}">${filelist[i]}</a></li>`;
//       i = i + 1;
//     }
//     list = list + '</ul>';
//     return list;
//   }
// }

//template 관련 함수들을 값으로 사용했다. ↑ (깔끔)
// function templateHTML(title, list, body, control) {
//   return ` 
//   <!doctype html>
//   <html>
//   <head>
//      <title>WEB1 - ${title}</title>
//      <meta charset="utf-8">
//   </head>
//    <body>
//      <h1><a href="/">WEB1</a></h1>
//      ${list}
//      ${control}
//      ${body}
//    </body>
//   </html>
//   `;
// }

// function templateList(filelist) {
//   var list = '<ul>';
//   var i = 0;
//   while (i < filelist.length) {
//     list = list + `<li><a href="/?id=${filelist[i]}">${filelist[i]}</a></li>`;
//     i = i + 1;
//   }
//   list = list + '</ul>';
//   return list;
// }

// request:웹 브라우저의 요청정보, response: 웹 서버의 응답 정보
var app = http.createServer(function (request, response) { // 웹브라우저의 접속이 있을때 마다 createServer의 콜백 함수를 NodeJS가 호출한다.
  console.log('hello');
  var _url = request.url;
  var queryData = url.parse(_url, true).query;
  var pathname = url.parse(_url, true).pathname;

  console.log(pathname); // '/'
  if (pathname === '/') { // home으로 갔느냐?
    if (queryData.id === undefined) { //id값을 선택 안했을 경우

      fs.readdir('./data', function (error, filelist) {
        var title = 'Welcome';
        var description = 'Hello, Node.js';

        var list = template.list(filelist); //template객체를 통해 정리정돈 가능
        var html = template.HTML(title, list,
          `<h2>${title}</h2>${description}`,
          `<a href="/create">create</a>`
        );
        response.writeHead(200); // 파일을 성공적으로 전송했다는 코드 200
        response.end(html);
      });
    } else { // 컨텐츠(id)를 선택했을 경우
      fs.readdir('./data', function (error, filelist) {
        var filteredId = path.parse(queryData.id).base; //외부에서 들어온 데이터를 필터로 걸러낸다.(오염된 데이터가 들어오는거 방지)
        //fs.readFile(`data/${queryData.id}`, 'utf8', function (err, description) { // 쿼리스트링에 따라 파일명이 생성된다.
        fs.readFile(`data/${filteredId}`, 'utf8', function (err, description) { //http://localhost:3000/?id=../password.js
          var title = queryData.id;
          //sanitize는 예민한 태그를 살균해버린다.
          var sanitizedTitle = sanitizeHtml(title);
          var sanitizedDescription = sanitizeHtml(description, {
            allowedTags:['h1']
          });
          var list = template.list(filelist);
          var html = template.HTML(sanitizedTitle, list,
            `<h2>${sanitizedTitle}</h2>${sanitizedDescription}`,
            `<a href="/create">create</a>
             <a href="/update?id=${sanitizedTitle}">update</a>
             <form action="delete_process" method="post">
                <input type="hidden" name="id" value="${sanitizedTitle}">
                <input type="submit" value="delete">
             </form>`
          );
          response.writeHead(200); // 파일을 성공적으로 전송
          response.end(html);
        });
      });
    }

  } else if (pathname === '/create') { // 최초 create버튼을 눌렀을 경우
    fs.readdir('./data', function (error, filelist) {
      var title = 'WEB - create';
      var list = template.list(filelist);
      var html = template.HTML(title, list, `
      <form action="/create_process" method="post"> 
         <p><input type="text" name="title" placeholder="title"></p> 
         <p>
            <textarea name="description" placeholder="description"></textarea>
         </p>
         <p>
            <input type="submit" value="전송">
         </p>
      </from>
      `, '');
      response.writeHead(200); // 파일을 성공적으로 전송했다는 코드 200
      response.end(html);
    })
  } else if (pathname === '/create_process') { // 다하고 create버튼을 눌렀을경우
    var body = '';
    request.on('data', function (data) { // 웹 브라우저가 post 엄청난 데이터를 웹 서버에 보냄 -> 서버쪽에서 콜백함수를 호출 -> data에 수신한 정보를 줌
      body = body + data; // 콜백함수가 실행될 때 마다 바디에 데이터를 추가한다.
    });
    request.on('end', function () { // 더이상 들어올 데이터가 없을때 즉, end면 서버의 데이터 수신이 끝났다.
      var post = qs.parse(body); //  post방식으로 받은 데이터를 파싱해서 객체화 한다.
      var title = post.title;
      var description = post.description;
      fs.writeFile(`data/${title}`, description, `utf8`, function (err) {//파일이 생성되면, 콜백함수가 실행된다.
        //response.writeHead(200);
        response.writeHead(302, { Location: `/?id=${title}` }); //리다이렉션 : 사용자의 요청처리후 특정 페이지로 튕겨버림
        response.end('Success');
      });
    });

  } else if (pathname === '/update') { // 최초 update 버튼을 눌렀을 때
    fs.readdir('./data', function (error, filelist) {
      var filteredId = path.parse(queryData.id).base; 
      fs.readFile(`data/${filteredId}`, 'utf8', function (err, description) {
        var title = queryData.id;
        var list = template.list(filelist);
        var html = template.HTML(title, list,
          `
          <form action="/update_process" method="post"> 
              <input type="hidden" name="id" value="${title}">
              <p><input type="text" name="title" placeholder="title" value=${title}></p> 
              <p>
                 <textarea name="description" placeholder="description">${description}</textarea>
              </p>
              <p>
                <input type="submit" value="수정">
              </p>
          </from>
          `,
          `<a href="/create">create</a> <a href="/update?id=${title}">update</a>`
        );
        response.writeHead(200); // 파일을 성공적으로 전송
        response.end(html);

      });
    });
  } else if (pathname === '/update_process') {// 업데이트 후 전송버튼 눌렀을 때
    var body = '';
    request.on('data', function (data) {
      body = body + data;
    });
    request.on('end', function () {
      var post = qs.parse(body);
      var id = post.id;
      var title = post.title;
      var description = post.description;
      console.log(post);
      fs.rename(`data/${id}`, `data/${title}`, function (error) {//파일 이름 바꾸기: old path, new path
        fs.writeFile(`data/${title}`, description, `utf8`, function (err) { //수정된 파일이름에 해당하는 description정보 가져오기
          response.writeHead(302, { Location: `/?id=${title}` }); //리다이렉트 코드 번호
          response.end('Success');
        });
      });
    });
  } else if (pathname === '/delete_process') {// 업데이트 후 전송버튼 눌렀을 때
    var body = '';
    request.on('data', function (data) {
      body = body + data;
    });
    request.on('end', function () {
      var post = qs.parse(body);
      var id = post.id;
      var filteredId = path.parse(id).base; //파일 지울때도 보안이 필요
      fs.unlink(`data/${filteredId}`, function (error) { //파일 지우는 함수
        response.writeHead(302, { Location: `/` }); //리다이렉트 설정
        response.end();
      });
    });
  } else { //웹 페이지에 접속이 안될때
    response.writeHead(404);
    response.end('Not found');
  }

});

app.listen(3000);