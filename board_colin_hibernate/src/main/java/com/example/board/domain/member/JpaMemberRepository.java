package com.example.board.domain.member;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {
    private final SessionFactory sessionFactory;

    public void save(Member member) {
        Session session = sessionFactory.getCurrentSession();

        session.save(member);
    }

    public void delete(String id) {
        Session session = sessionFactory.getCurrentSession();

        Member member = session.find(Member.class, id);
        session.remove(member);

    }

    public Long countByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.openSession();

        Long cnt = (Long) session.createQuery("SELECT COUNT(m) FROM Member m WHERE m.username = :username AND m.password = :password")
                .setParameter("username", username).setParameter("password", password).getSingleResult();

        return cnt;
    }

    public void deleteByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            Member member = findByUsername(username).get();
            session.remove(member);
           /*
            session.createQuery("DELETE FROM Member m WHERE m.username = :username AND m.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password);
*/
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            session.close();
        }
    }

    public Optional<Member> findByUsername(String username) {
        Session session = sessionFactory.openSession();
        TypedQuery<Member> member = session.createQuery("SELECT m FROM Member m WHERE m.username = :username")
                .setParameter("username", username);

        Optional<Member> o = Optional.of(member.getSingleResult());

        session.close();

        return o;
    }

    public Optional<Member> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Optional<Member> o = Optional.of(session.find(Member.class, id));

        return o;
    }
}
