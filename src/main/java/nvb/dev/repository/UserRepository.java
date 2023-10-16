package nvb.dev.repository;

import nvb.dev.base.repository.BaseRepository;
import nvb.dev.entity.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<Long, User> {

    Optional<User> findUserByFirstName(String firstName);

    Optional<User> findUserByUsername(String username);

    Optional<User> findByPassword(String password);

}
