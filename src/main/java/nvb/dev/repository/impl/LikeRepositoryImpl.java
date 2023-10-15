package nvb.dev.repository.impl;

import nvb.dev.base.repository.impl.BaseRepositoryImpl;
import nvb.dev.entity.Like;
import nvb.dev.repository.LikeRepository;
import org.hibernate.Session;

public class LikeRepositoryImpl extends BaseRepositoryImpl<Long, Like> implements LikeRepository {

    protected final Session session;

    public LikeRepositoryImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public Class<Like> getEntityClass() {
        return Like.class;
    }
}
