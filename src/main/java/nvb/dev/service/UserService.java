package nvb.dev.service;

import nvb.dev.base.service.BaseService;
import nvb.dev.entity.User;

import java.util.Optional;

public interface UserService extends BaseService<Long, User> {

    void signUp(User user);

    boolean isValid(User user);

    Optional<User> findUserByFirstName(String firstName);

    Optional<User> findUserByUsername(String username);

    Optional<User> findByPassword(String password);

}
