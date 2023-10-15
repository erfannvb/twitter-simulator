package nvb.dev.service.impl;

import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.Like;
import nvb.dev.repository.LikeRepository;
import nvb.dev.service.LikeService;
import org.hibernate.Session;

public class LikeServiceImpl extends BaseServiceImpl<Long, Like, LikeRepository>
        implements LikeService {

    protected final Session session;

    public LikeServiceImpl(Session session, LikeRepository repository) {
        super(session, repository);
        this.session = session;
    }
}
