package com.example.board.dto;

import com.example.board.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberDto implements UserDetails {
    private Long id;

    @NotBlank(message = "Id는 필수 입력값 입니다.")
    @Size(min = 4, max = 15, message = "Id를 4 ~ 15자 사이로 입력해주세요")
    private String username;

    @NotBlank(message = "Password는 필수 입력값 입니다.")
    @Size(min = 4, max = 15, message = "Password를 4 ~ 15자 사이로 입력해주세요")
    private String password;

    private String role;

    @Builder
    public MemberDto(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
