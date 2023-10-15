package nvb.dev.repository.impl;

import nvb.dev.base.repository.impl.BaseRepositoryImpl;
import nvb.dev.entity.Comment;
import nvb.dev.repository.CommentRepository;
import org.hibernate.Session;

public class CommentRepositoryImpl extends BaseRepositoryImpl<Long, Comment>
        implements CommentRepository {

    protected final Session session;

    public CommentRepositoryImpl(Session session) {
        super(session);
        this.session = session;
    }

    @Override
    public Class<Comment> getEntityClass() {
        return Comment.class;
    }
}
