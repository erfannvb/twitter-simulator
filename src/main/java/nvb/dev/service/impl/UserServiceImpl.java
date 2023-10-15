package nvb.dev.service.impl;

import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.User;
import nvb.dev.repository.UserRepository;
import nvb.dev.service.UserService;
import org.hibernate.Session;

public class UserServiceImpl extends BaseServiceImpl<Long, User, UserRepository> implements UserService {

    protected final Session session;

    public UserServiceImpl(Session session, UserRepository repository) {
        super(session, repository);
        this.session = session;
    }
}
