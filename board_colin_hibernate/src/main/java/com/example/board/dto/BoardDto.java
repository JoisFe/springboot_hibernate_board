package com.example.board.dto;

import com.example.board.domain.board.Board;
import com.example.board.domain.member.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto {
    private Long id;
    private Member author; // User 의 userId를 가리키는 외래키임 User의 userId는 고유키이기 때문에 외래키로 연결 가능

    @NotBlank(message = "제목은 필수 입력값 입니다.")
    @Size(min = 1, max = 30, message = "제목을 1 ~ 30자 사이로 입력해주세요")
    private String title;

    @Size(min = 0, max = 300, message = "내용을 300자 이하로 입력해주세요")
    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardDto(Long id, Member author, String title, String content, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;

    }

    public Board toEntity() {
        return Board.builder()
                .id(id)
                .author(author)
                .title(title)
                .content(content)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }
}
