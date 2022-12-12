package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.domain.SearchCondition;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id") != null;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute SearchCondition sc, Model m, HttpServletRequest request) {
        if (!loginCheck(request))
            return "redirect:/login/login?toURL=" + request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동


        try {
            int totalCnt = boardService.getSearchResultCnt(sc);
            m.addAttribute("totalCnt", totalCnt);

            PageHandler pageHandler = new PageHandler(totalCnt, sc);

            List<BoardDto> list = boardService.getSearchResultPage(sc);
            m.addAttribute("list", list);
            m.addAttribute("ph", pageHandler);

            Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            m.addAttribute("startOfToday", startOfToday.toEpochMilli());
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg", "LIST_ERR");
            m.addAttribute("totalCnt", 0);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }


    @GetMapping("/read")
    public String read(Integer bno, Integer page, Integer pageSize, Model m) {
        try {
            BoardDto boardDto = boardService.read(bno);
            // m.addAttribute("boardDto", boardDto); // 아래 문장과 동일
            m.addAttribute(boardDto);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg", "READ_ERR");
        }

        return "board";
    }

    @PostMapping("remove")
    public String remove(Integer bno, Integer page, Integer pageSize, Model m, HttpSession session, RedirectAttributes rattr) {
        String writer = (String) session.getAttribute("id");
        try {
            int rowCnt = boardService.remove(bno, writer);
            m.addAttribute("page", page);
            m.addAttribute("pageSize", pageSize);

            if (rowCnt != 1) {
                // 삭제하다가 에러 발생시 catch구문으로 예외를 던짐
                throw new Exception("[Board Remove Error!!]");
            }

            //m.addAttribute("msg", "DEL_OK");
            // 세션에 1번저장 후 사라짐 -> 메시지창 새로고침 해도 1번만 나옴
            rattr.addFlashAttribute("msg", "DEL_OK");
        } catch (Exception e) {
            e.printStackTrace();
            // 삭제 하면서 에러 발생시 예외처리
            // m.addAttribute("msg", "DEL_ERROR");
            // 삭제실패 후 URL에 &msg=DEL_ERROR 안보임
            rattr.addFlashAttribute("msg", "DEL_ERR");
        }


        // model에 담으면 redirect할때 뒤에 붙음
        return "redirect:/board/list";
    }

    // 게시물 보기 모드
    @GetMapping("/write")
    public String write(Model m) {
        // 글쓰기일때만 mode값을 넘겨줌
        m.addAttribute("mode", "new");
        return "board"; // borad.jsp는 읽기와 쓰기에 사용, 쓰기일때 mode=new
    }

    // 게시물 쓰기 모드
    @PostMapping("/write")
    public String write(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes rattr) {
        String writer = (String)session.getAttribute("id");
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.write(boardDto); // insert

            if(rowCnt != 1) {
                // 예외1) 글write 에러시 예외로 보내서
                throw new Exception("[Fail Write!!]");
            }

            // 글쓰기 성공
            rattr.addFlashAttribute("msg", "WRT_OK");
            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();

            // 예외2) 실패시 성한 내용 다시 보여줌
            m.addAttribute("boardDto", boardDto);
            m.addAttribute("msg", "WRT_ERR");
            return "board";
        }
    }

    // 게시물 수정 모드
    @PostMapping("/modify")
    public String modify(BoardDto boardDto, Model m, HttpSession session, RedirectAttributes rattr) {
        String writer = (String)session.getAttribute("id");
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.modify(boardDto); // insert

            if(rowCnt != 1) {
                // 예외1) 글write 에러시 예외로 보내서
                throw new Exception("[Fail Modify!!]");
            }

            // 글쓰기 성공
            rattr.addFlashAttribute("msg", "MOD_OK");
            return "redirect:/board/list";
        } catch (Exception e) {
            e.printStackTrace();

            // 예외2) 실패시 성한 내용 다시 보여줌
            m.addAttribute("boardDto", boardDto);
            m.addAttribute("msg", "MOD_ERR");
            return "board";
        }
    }
}