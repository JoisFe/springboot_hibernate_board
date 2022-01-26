package com.example.board.service;

import com.example.board.domain.Criteria;
import com.example.board.domain.board.Board;
import com.example.board.domain.comment.Comment;
import com.example.board.domain.comment.JpaCommentRepository;
import com.example.board.dto.BoardDto;
import com.example.board.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final JpaCommentRepository commentRepository;

    public Long commentListCnt(Board board) {
        return commentRepository.countByBoardId(board);
    }

    public List<CommentDto> commentList(BoardDto boardDto, Criteria cri) {

        List<Comment> comments = commentRepository.findAllByBoardId(boardDto.toEntity(), cri); // /마찬가지 boardId 는 Long타입이 아니라 Board 타입이라 문제가됨

        List<CommentDto> commentDtoList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto commentDto = CommentDto.builder()
                    .id(comment.getId())
                    .boardId(comment.getBoardId())
                    .content(comment.getContent())
                    .author(comment.getAuthor())
                    .createdDate(comment.getCreatedDate())
                    .modifiedDate(comment.getModifiedDate())
                    .build();

            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }

    public CommentDto getComment(Long id) {
        Optional<Comment> commentWrapper = commentRepository.findById(id);
        Comment comment = commentWrapper.get();

        return CommentDto.builder()
                .id(comment.getId())
                .boardId(comment.getBoardId())
                .content(comment.getContent())
                .author(comment.getAuthor())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }


    @Transactional
    public void uploadComment(CommentDto commentDto) {
        commentDto.setCreatedDate(LocalDateTime.now());
        commentDto.setModifiedDate(LocalDateTime.now());

        commentRepository.save(commentDto.toEntity());
    }

    @Transactional
    public void updateComment(CommentDto commentDto) {
        commentDto.setModifiedDate(LocalDateTime.now());

        commentRepository.update(commentDto.getId(), commentDto.getContent());
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.delete(id);
    }

}
