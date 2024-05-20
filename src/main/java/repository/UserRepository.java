package repository;

import domain.User.User;
import java.util.UUID;

public class UserRepository extends AbstractRepository<User> {

    @Override
    public User findById(UUID id) {
        return null;
    }
}
