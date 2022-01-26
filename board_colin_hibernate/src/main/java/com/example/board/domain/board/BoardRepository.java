package com.example.board.domain.board;

import com.example.board.domain.Criteria;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    void save(Board board);

    void delete(Long id);

    void update(Long id, String title, String content);

    Long count();

    List<Board> findAllGreaterThan(Criteria cir);

    Optional<Board> findById(Long id);
}
