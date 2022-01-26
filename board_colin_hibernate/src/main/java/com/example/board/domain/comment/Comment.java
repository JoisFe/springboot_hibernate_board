package com.example.board.domain.comment;

import com.example.board.domain.board.Board;
import com.example.board.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "boardId", nullable = false)
    private Board boardId;

    @Column(columnDefinition = "TEXT", length = 300, nullable = false)
    private String content;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author", nullable = false)
    private Member author;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


    @Builder
    public Comment(Long id, Board boardId, String content, Member author, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.boardId = boardId;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
