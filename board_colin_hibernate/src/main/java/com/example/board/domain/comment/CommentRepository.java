package com.example.board.domain.comment;

import com.example.board.domain.Criteria;
import com.example.board.domain.board.Board;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    void save(Comment comment);

    void delete(Long id);

    void update(Long id, String content);

    Optional<Comment> findById(Long id);

    List<Comment> findAllByBoardId(Board board, Criteria cri);

    Long countByBoardId(Board board);

}
