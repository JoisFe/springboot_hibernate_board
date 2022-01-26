package com.example.board.controller;

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

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final BoardService boardService;

    @PostMapping("comment")
    @ResponseBody
    public String uploadComment(Long author, Long boardId, @Valid CommentDto commentDto, BindingResult errors, HttpServletRequest req) {
        commentDto.setBoardId(boardService.getBoard(boardId).toEntity());
        commentDto.setAuthor(memberService.getMember(author).toEntity());
        if (!memberService.commentWriterTest(commentDto.getAuthor().getUsername(), req))
            return "현재 로그인한 아이디와 작성자가 다릅니다.";

        if (errors.hasErrors()) {
            if (!boardService.validTest(errors, memberService).equals("ok"))
                return boardService.validTest(errors, memberService);
        }

        commentService.uploadComment(commentDto);

        return "success";
    }

    @GetMapping("/commentView")
    public String viewComment(Model model, Long id, HttpServletRequest req) {
        model.addAttribute("list", commentService.getComment(id));

        String userId = memberService.findSessionId(req);
        model.addAttribute("id", userId);

        return "boards/commentView";
    }

    @PostMapping("/updateComment")
    @ResponseBody
    public String updateComment(Long author, Long boardId, @Valid CommentDto commentDto, BindingResult errors, HttpServletRequest req) {
        commentDto.setBoardId(boardService.getBoard(boardId).toEntity());
        commentDto.setAuthor(memberService.getMember(author).toEntity());

        if (!memberService.commentWriterTest(commentDto.getAuthor().getUsername(), req))
            return "현재 로그인한 아이디와 작성자가 다릅니다.";

        if (errors.hasErrors()) {
            if (!boardService.validTest(errors, memberService).equals("ok"))
                return boardService.validTest(errors, memberService);
        }

        commentService.updateComment(commentDto);

        return "success";
    }

    @PostMapping("/deleteComment")
    @ResponseBody
    public String deleteComment(Long id, Long boardId, String authorName, HttpServletRequest req) {
        if (!memberService.commentWriterTest(authorName, req)) return "현재 로그인한 아이디와 작성자가 다릅니다.";

        commentService.deleteComment(id);

        return "success";

    }
}
