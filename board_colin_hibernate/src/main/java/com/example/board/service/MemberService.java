package com.example.board.service;


import com.example.board.domain.member.JpaMemberRepository;
import com.example.board.domain.member.Member;
import com.example.board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final JpaMemberRepository memberRepository;

    public void register(MemberDto memberDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        memberRepository.save(memberDto.toEntity());
    }


    public void memberDelete(MemberDto memberDto) {
        memberRepository.deleteByUsernameAndPassword(memberDto.getUsername(), memberDto.getPassword());
    }

    public Member findId(MemberDto memberDto) {
        return memberRepository.findByUsername(memberDto.getUsername()).get();
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    public Long idCheck(MemberDto memberDto) {
        return memberRepository.countByUsernameAndPassword(memberDto.getUsername(), memberDto.getPassword());
    }


    public String findSessionId(HttpServletRequest req) {
        HttpSession session = req.getSession();

        return (String) session.getAttribute("id");
    }

    public String registerValidTest(Model model, @Valid MemberDto memberDto, BindingResult errors, String samePassword) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));

                return validatorResult.get(key);
            }
        }

        if (idCheck(memberDto) == 1) return "????????? ???????????? ???????????????.";

        register(memberDto);

        return "success";
    }

    public boolean boardWriterTest(String author, HttpServletRequest req) {
        String sessionId = findSessionId(req);

        return author.equals(sessionId); //Author??? ????????? User?????? ??? ????????? AUthor??? ????????? getAuthor??? 2??? ???
    }

    public boolean commentWriterTest(String author, HttpServletRequest req) {
        String sessionId = findSessionId(req);
        return author.equals(sessionId);
    }

    public Member getUser(String author) {

        return memberRepository.findByUsername(author).get();
    }

    public MemberDto getMember(Long id) {
        Optional<Member> memberWrapper = memberRepository.findById(id);
        Member member = memberWrapper.get();

        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
