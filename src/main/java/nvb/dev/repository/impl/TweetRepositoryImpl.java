package nvb.dev.repository.impl;

import nvb.dev.base.repository.impl.BaseRepositoryImpl;
import nvb.dev.entity.Tweet;
import nvb.dev.repository.TweetRepository;
import org.hibernate.Session;

public class TweetRepositoryImpl extends BaseRepositoryImpl<Long, Tweet> implements TweetRepository {

    protected final Session session;

    public TweetRepositoryImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public Class<Tweet> getEntityClass() {
        return Tweet.class;
    }
}
