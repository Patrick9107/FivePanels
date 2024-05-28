package repository;

import domain.User.User;
import domain.User.UserException;
import domain.common.BaseEntity;

import java.util.HashMap;
import java.util.UUID;
import static foundation.Assert.*;

public abstract class UserRepository {

    private static HashMap<UUID, User> users = new HashMap<>();
    public static User findById(UUID id) {
        if(!users.containsKey(id))
            throw new UserException("findById(): id does not exist");
        return users.get(id);
    }


    public static void save(User user) {
        isNotNull(user, "user");
        users.put(user.getId(), user);
    }
}
