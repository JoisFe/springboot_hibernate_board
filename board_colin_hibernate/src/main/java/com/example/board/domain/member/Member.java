package com.example.board.domain.member;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    @Builder
    public Member(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;

    }

}
