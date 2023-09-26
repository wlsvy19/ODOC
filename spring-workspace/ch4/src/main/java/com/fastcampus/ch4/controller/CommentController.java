package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.CommentDto;
import com.fastcampus.ch4.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    // 지정된 게시물의 모든 댓글을 가져오는 메서드
    @GetMapping("/comments") // comments?bno=1080   GET메서드
    //@ResponseBody
    public ResponseEntity<List<CommentDto>> list(Integer bno) {
        List<CommentDto> list = null;
        try {
            list = commentService.getList(bno);
            // ResponseEntity: entity에 HTTP 코드도 같이 보내기 위해 사용
            // service에서 예외 발생해도 응답코드는 200으로 나옴, 구분하기 위해 사용
            return new ResponseEntity<List<CommentDto>>(list, HttpStatus.OK); // 200
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<CommentDto>>(HttpStatus.BAD_REQUEST); // 400
        }
        //return list; // list.jsp로 인식-> @ResponseBody 붙여야함
    }

    // 댓글을 등록하는 메서드
    @PostMapping("/comments")
    //@ResponseBody
    public ResponseEntity<String> write(@RequestBody CommentDto dto, Integer bno, HttpSession session) { // @RequestBody 를 붙여야 JSON들어온걸 JAVA객체로 자동 변환함
        // String commenter = (String) session.getAttribute("id");
        // TODO: 세션을 이용하여 로그인 후 댓글 작성 하도록 수정
        String commenter = "asdf";
        dto.setCommenter(commenter); // 작성자 지정
        dto.setBno(bno); // 어느 게시물에 댓글 달았는지 지정
        try {
            if (commentService.write(dto) != 1) {
                throw new Exception("Comments Write Failed!!");
            }
            return new ResponseEntity<String>("WRT_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("WRT_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글을 수정하는 메서드
    @PatchMapping("/comments/{cno}")   // 수정할 대상 필요
    //@ResponseBody
    public ResponseEntity<String> modify(@PathVariable Integer cno, @RequestBody CommentDto dto, HttpSession session) { // @RequestBody 를 붙여야 JSON들어온걸 JAVA객체로 자동 변환함
        // String commenter = (String) session.getAttribute("id");
        // TODO: 세션을 이용하여 로그인 후 댓글 수정 하도록 수정
        String commenter = "asdf";
        dto.setCommenter(commenter); // 작성자 지정
        dto.setCno(cno);
        try {
            if (commentService.modify(dto) != 1) {
                throw new Exception("Comments Write Failed!!");
            }
            return new ResponseEntity<String>("MOD_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("MOD_ERR", HttpStatus.BAD_REQUEST);
        }
    }

    // 지정된 댓글을 삭제하는 메서드
    @DeleteMapping("/comments/{cno}") // comments/1?bno=1080     * 쿼리스트링 형식이 아니라 URI 일부에 포함된거면 @PathVariable 사용 해야함
    //@ResponseBody
    public ResponseEntity<String> remove(@PathVariable Integer cno, Integer bno, HttpSession session) {
        // String commenter = (String) session.getAttribute("id");
        String commenter = "qwer";
        try {
            int rowCnt = commentService.remove(cno, bno, commenter);

            if (rowCnt != 1) {
                throw new Exception("Delete Failed!");
            }

            return new ResponseEntity<>("DEL_OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("DEL_ERR", HttpStatus.BAD_REQUEST);
        }
    }
}
