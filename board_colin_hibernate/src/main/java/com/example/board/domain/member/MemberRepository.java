package com.example.board.domain.member;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    void delete(String id);

    Long countByUsernameAndPassword(String username, String password);

    void deleteByUsernameAndPassword(String username, String password);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(Long id);
}
