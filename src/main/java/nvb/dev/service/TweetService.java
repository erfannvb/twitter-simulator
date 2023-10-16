package nvb.dev.service;

import nvb.dev.base.service.BaseService;
import nvb.dev.entity.Tweet;
import nvb.dev.entity.User;

public interface TweetService extends BaseService<Long, Tweet> {
    boolean isValid(Tweet tweet);
}
