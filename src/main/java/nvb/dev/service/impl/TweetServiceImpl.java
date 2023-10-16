package nvb.dev.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.Tweet;
import nvb.dev.entity.User;
import nvb.dev.exception.ValidationException;
import nvb.dev.repository.TweetRepository;
import nvb.dev.service.TweetService;
import org.hibernate.Session;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;

public class TweetServiceImpl extends BaseServiceImpl<Long, Tweet, TweetRepository> implements TweetService {

    protected final Session session;

    public TweetServiceImpl(Session session, TweetRepository repository) {
        super(session, repository);
        this.session = session;
    }

    @Override
    public boolean isValid(Tweet tweet) {
        try {

            ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory();

            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<Tweet>> violationSet = validator.validate(tweet);
            if (!violationSet.isEmpty()) {
                for (ConstraintViolation<Tweet> violation : violationSet) {
                    System.out.println(violation.getMessage());
                }
                validatorFactory.close();
                return false;
            } else {
                return true;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
