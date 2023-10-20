package nvb.dev.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nvb.dev.base.service.impl.BaseServiceImpl;
import nvb.dev.entity.User;
import nvb.dev.exception.ValidationException;
import nvb.dev.repository.UserRepository;
import nvb.dev.service.UserService;
import org.hibernate.Session;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Optional;
import java.util.Set;

public class UserServiceImpl extends BaseServiceImpl<Long, User, UserRepository> implements UserService {

    protected final Session session;

    public UserServiceImpl(Session session, UserRepository repository) {
        super(session, repository);
        this.session = session;
    }

    @Override
    public void signUp(User user) {
        session.getTransaction().begin();
        repository.save(user);
        session.getTransaction().commit();
    }

    @Override
    public boolean isValid(User user) {
        try {

            ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                    .configure()
                    .messageInterpolator(new ParameterMessageInterpolator())
                    .buildValidatorFactory();

            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<User>> violationSet = validator.validate(user);
            if (!violationSet.isEmpty()) {
                for (ConstraintViolation<User> violation : violationSet) {
                    System.out.println(violation.getMessage());
                }
                validatorFactory.close();
                return false;
            } else {
                signUp(user);
                return true;
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> findUserByFirstName(String firstName) {
        return repository.findUserByFirstName(firstName);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public Optional<User> findByPassword(String password) {
        return repository.findByPassword(password);
    }

}
