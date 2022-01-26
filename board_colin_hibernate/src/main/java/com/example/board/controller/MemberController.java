package com.example.board.controller;

import com.example.board.dto.MemberDto;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;

    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }


    @ResponseBody
    @PostMapping("/register")
    public String register(Model model, @Valid MemberDto memberDto, BindingResult errors, String samePassword) { // @Valid User 다음 바로 BindingResult 나와야함 다른 매개변수가 중간에 있을시 에러나도 잡지를 못함
        memberDto.setRole("ROLE_USER");
        try {
            if (!memberDto.getPassword().equals(samePassword)) return "두 비빌먼호가 다릅니다.";
            return service.registerValidTest(model, memberDto, errors, samePassword);
        } catch (Exception e) {
            if (memberDto.getId() == null) return service.registerValidTest(model, memberDto, errors, samePassword);

            return "에러가 발생하였습니다.";
        }
    }

    @GetMapping("/loginFail")
    public String loginFail() {
        return "user/loginFail";
    }


    @GetMapping("/")
    public String loginForm(Model model, HttpServletRequest req) {
        HttpSession session = req.getSession();

        String fail = (String) session.getAttribute("fail");

        model.addAttribute("fail", fail);

        session.removeAttribute("fail");

        return "user/login";
    }


    @GetMapping("/memberDelete")
    public String memberDelete(Model model, HttpServletRequest req) {
        String id = service.findSessionId(req);

        model.addAttribute("id", id);

        return "user/memberDelete";
    }

    @PostMapping("/memberDelete")
    @ResponseBody
    public String memberDelete(MemberDto memberDto) {

        service.memberDelete(memberDto);

        return "success";
    }
}
