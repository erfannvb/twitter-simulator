package nvb.dev.repository.impl;

import nvb.dev.base.repository.impl.BaseRepositoryImpl;
import nvb.dev.entity.User;
import nvb.dev.repository.UserRepository;
import org.hibernate.Session;

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
}
