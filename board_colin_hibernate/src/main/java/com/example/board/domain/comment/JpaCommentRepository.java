package com.example.board.domain.comment;

import com.example.board.domain.Criteria;
import com.example.board.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    private final SessionFactory sessionFactory;

    public void save(Comment comment) {
        Session session = sessionFactory.getCurrentSession();

        session.save(comment);
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Comment comment = session.find(Comment.class, id);
        session.remove(comment);

    }

    public void update(Long id, String content) {
        Session session = sessionFactory.getCurrentSession();

        Comment comment = session.find(Comment.class, id);
        comment.setContent(content);
    }

    public Optional<Comment> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.of(session.find(Comment.class, id));
    }

    public List<Comment> findAllByBoardId(Board board, Criteria cri) {
        Session session = sessionFactory.getCurrentSession();

        List<Comment> list = (List<Comment>) session.createQuery("SELECT c FROM Comment c WHERE c.boardId = :board order by c.createdDate DESC").setParameter("board", board)
                .setFirstResult(cri.getPageStart())
                .setMaxResults(cri.getPerPageNum())
                .getResultList();

        return list;
    }

    public Long countByBoardId(Board board) {
        Session session = sessionFactory.getCurrentSession();

        Long cnt = (Long) session.createQuery("SELECT count(c) FROM Comment c WHERE c.boardId = :board").setParameter("board", board).getSingleResult();

        return cnt;
    }

}
