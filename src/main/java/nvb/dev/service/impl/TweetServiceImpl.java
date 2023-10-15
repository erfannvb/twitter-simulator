package nvb.dev.service.impl;

import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.Tweet;
import nvb.dev.repository.TweetRepository;
import org.hibernate.Session;

public class TweetServiceImpl extends BaseServiceImpl<Long, Tweet, TweetRepository> implements TweetRepository {

    protected final Session session;

    public TweetServiceImpl(Session session, TweetRepository repository) {
        super(session, repository);
        this.session = session;
    }
}
