package com.example.board.dto;

import com.example.board.domain.board.Board;
import com.example.board.domain.comment.Comment;
import com.example.board.domain.member.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private Board boardId;

    @NotBlank(message = "내용은 필수 입력값 입니다.")
    @Size(min = 1, max = 300, message = "내용을 0자 ~ 300자로 입력해주세요")
    private String content;

    private Member author;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public CommentDto(Long id, Board boardId, String content, Member author, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.boardId = boardId;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public Comment toEntity() {
        return Comment.builder()
                .id(id)
                .boardId(boardId)
                .content(content)
                .author(author)
                .createdDate(createdDate)
                .modifiedDate(modifiedDate)
                .build();
    }
}
