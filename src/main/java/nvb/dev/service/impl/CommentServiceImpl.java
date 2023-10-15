package nvb.dev.service.impl;

import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.Comment;
import nvb.dev.repository.CommentRepository;
import nvb.dev.service.CommentService;
import org.hibernate.Session;

public class CommentServiceImpl extends BaseServiceImpl<Long, Comment, CommentRepository>
        implements CommentService {

    protected final Session session;

    public CommentServiceImpl(Session session, CommentRepository repository) {
        super(session, repository);
        this.session = session;
    }
}
