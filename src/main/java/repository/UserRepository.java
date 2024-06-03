package repository;

import domain.User.User;
import domain.User.UserException;

import java.time.Instant;
import java.util.*;

/**
 * A repository class for managing user entities.
 */
public class UserRepository {

    /**
     * A hashmap for storing user entities with their uuid as the key
     */
    private static final HashMap<UUID, User> map = new HashMap<>();

    /**
     * Finds an entity by its ID.
     * @param id the ID of the entity to find
     * @return an Optional containing the entity if found, or an empty Optional if no entity is found
     */
    public static Optional<User> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Retrieves all entities stored in the repository.
     * @return a list of all entities
     */
    public static List<User> findAll() {
        return new ArrayList<>(map.values());
    }
    /**
     * Saves an entity in the repository.
     * @param entity the entity to save or update
     * @return the saved or updated entity
     */
    public static User save(User entity) {
        map.put(entity.getId(), entity);
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    /**
     * Counts the number of entities in the repository.
     * @return the total number of entities stored
     */
    public static int count() {
        return map.size();
    }

    /**
     * Deletes an entity by its ID.
     * @param id the ID of the entity to delete
     */
    public static void deleteById(UUID id) {
        map.remove(id);
    }

    /**
     * Checks if an entity exists in the repository by its ID.
     * @param id the ID of the entity to check
     * @return true if the entity exists, false otherwise
     */
    public static boolean existsById(UUID id) {
        return map.get(id) != null;
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for.
     * @return an Optional containing the found user if a user with the given email exists,
     *         otherwise an empty Optional.
     */
    public static Optional<User> findByEmail(String email) {
        return findAll().stream().filter(user -> user.getEmail().getAddress().equals(email)).findFirst();
    }
}
