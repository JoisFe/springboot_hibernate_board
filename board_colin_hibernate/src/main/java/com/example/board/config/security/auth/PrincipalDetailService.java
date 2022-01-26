package com.example.board.config.security.auth;

import com.example.board.domain.member.JpaMemberRepository;
import com.example.board.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailService implements UserDetailsService {

    private final JpaMemberRepository memberRepository;

    //스프링이 로그인 요청을 가로챌때 username, password변수 2개를 가로채는데
    //password 부분 처리는 알아서처리
    //username이 DB에 있는지 확인해줘야함 !!!
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member principal = memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다.:" + username);
                });
        return new PrincipalDetail(principal); //시큐리티의 세션에 유저정보가 저장이 됨
    }
}