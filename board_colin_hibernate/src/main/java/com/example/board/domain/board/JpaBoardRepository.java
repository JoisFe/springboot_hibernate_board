package com.example.board.domain.board;

import com.example.board.domain.Criteria;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBoardRepository implements BoardRepository {
    private final SessionFactory sessionFactory;

    public void save(Board board) {
        Session session = sessionFactory.getCurrentSession();

        session.save(board);

    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Board board = session.find(Board.class, id);
        session.remove(board);

    }

    public void update(Long id, String title, String content) {
        Session session = sessionFactory.getCurrentSession();

        Board board = session.find(Board.class, id);
        board.setTitle(title);
        board.setContent(content);
    }

    public Long count() {
        Session session = sessionFactory.getCurrentSession();

        Long cnt = (Long) session.createQuery("SELECT COUNT(b) FROM Board b").getSingleResult();

        return cnt;
    }

    public List<Board> findAllGreaterThan(Criteria cri) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "FROM Board as b ORDER BY b.id DESC";
        Query query = session.createQuery(hql)
                .setFirstResult(cri.getPageStart())
                .setMaxResults(cri.getPerPageNum());

        List results = query.list();

        /*
        List<Board> list = session.createQuery("SELECT b FROM Board b order by b.modifiedDate DESC")
                .setFirstResult(cri.getPageStart())
                .setMaxResults(cri.getPerPageNum())
                .getResultList();
*/

        return results;
    }

    public Optional<Board> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return Optional.of(session.find(Board.class, id));
    }
}
