package com.example.board.controller;

import com.example.board.domain.Criteria;
import com.example.board.domain.Paging;
import com.example.board.dto.BoardDto;
import com.example.board.dto.CommentDto;
import com.example.board.service.BoardService;
import com.example.board.service.CommentService;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private Object IllegalStateException;

    @GetMapping("/main")
    public String main(Model model, Criteria cri, HttpServletRequest req) {
        String id = memberService.findSessionId(req);
        model.addAttribute("id", id);

        return "boards/main";
    }

    @PostMapping("/main")
    @ResponseBody
    public Map<String, Object> main(Criteria cri) {
        Long boardListCnt = boardService.boardListCnt();

        Paging paging = new Paging(cri, boardListCnt);

        List<BoardDto> list = boardService.boardList(cri);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("list", list);
        map.put("paging", paging);

        return map;
    }

    @GetMapping("/view")
    public String viewBoard(Model model, Long id, HttpServletRequest req) {
        model.addAttribute("view", boardService.getBoard(id));

        String userId = memberService.findSessionId(req);
        model.addAttribute("id", userId);

        if (userId != null) model.addAttribute("userAuthorId", memberService.getUser(userId).getId());

        return "boards/view";
    }

    @PostMapping("/view")
    @ResponseBody
    public Map<String, Object> viewBoard(Long id, Criteria cri) {
        BoardDto boardDto = boardService.getBoard(id);

        Long commentListCnt = commentService.commentListCnt(boardDto.toEntity()); //-> 지금 entity를 보면 boardId가 Long 타입이 아니라 Board 타입이라 문제가 됨

        Paging paging = new Paging(cri, commentListCnt);

        List<CommentDto> list = commentService.commentList(boardDto, cri);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("list", list);
        map.put("paging", paging);

        return map;
    }

    @GetMapping("/upload")
    public String uploadBoardForm(Model model, HttpServletRequest req) {
        String id = memberService.findSessionId(req);

        model.addAttribute("id", id);

        model.addAttribute("authorId", memberService.getUser(id).getId());

        return "boards/upload";
    }


    @PostMapping("/upload")
    @ResponseBody
    public String uploadBoard(Long author, @Valid BoardDto boardDto, BindingResult errors, HttpServletRequest req) {
        boardDto.setAuthor(memberService.getMember(author).toEntity());
        if (!memberService.boardWriterTest(boardDto.getAuthor().getUsername(), req)) return "현재 로그인한 아이디와 작성자가 다릅니다.";

        if (errors.hasErrors()) {
            if (!boardService.validTest(errors, memberService).equals("ok"))
                return boardService.validTest(errors, memberService);
        }

        boardService.uploadBoard(boardDto);

        return "success";
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateBoard(Long author, @Valid BoardDto boardDto, BindingResult errors, HttpServletRequest req) {
        boardDto.setAuthor(memberService.getMember(author).toEntity());
        if (!memberService.boardWriterTest(boardDto.getAuthor().getUsername(), req)) return "현재 로그인한 아이디와 작성자가 다릅니다.";

        if (errors.hasErrors()) {
            if (!boardService.validTest(errors, memberService).equals("ok"))
                return boardService.validTest(errors, memberService);
        }
        boardService.updateBoard(boardDto);

        return "success";
    }

    @PostMapping("/delete")
    @ResponseBody
    public String deleteBoard(Long id, String author, HttpServletRequest req) {

        if (!memberService.boardWriterTest(author, req)) return "현재 로그인한 아이디와 작성자가 다릅니다.";

        boardService.deleteBoard(id);

        return "success";
    }
}
