package nvb.dev.repository.impl;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import nvb.dev.base.repository.impl.BaseRepositoryImpl;
import nvb.dev.entity.User;
import nvb.dev.repository.UserRepository;
import org.hibernate.Session;

import java.util.Optional;

public class UserRepositoryImpl extends BaseRepositoryImpl<Long, User> implements UserRepository {

    protected final Session session;

    public UserRepositoryImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public Optional<User> findUserByFirstName(String firstName) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.equal(root.get("firstName"), firstName));

        TypedQuery<User> userTypedQuery = session.createQuery(query);
        User result = userTypedQuery.getSingleResult();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.equal(root.get("username"), username));

        TypedQuery<User> userTypedQuery = session.createQuery(query);
        User result = userTypedQuery.getSingleResult();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findUserByAge(int age) {
        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(builder.equal(root.get("age"), age));

        TypedQuery<User> userTypedQuery = session.createQuery(query);
        User result = userTypedQuery.getSingleResult();

        return Optional.ofNullable(result);
    }
}
