package repository;

import domain.Messenger.Chat;
import domain.User.User;

import java.time.Instant;
import java.util.*;

/**
 * A repository class for managing user entities.
 */
public class ChatRepository {

    /**
     * A hashmap for storing user entities with their uuid as the key
     */
    private static final HashMap<UUID, Chat> map = new HashMap<>();

    /**
     * Finds an entity by its ID.
     * @param id the ID of the entity to find
     * @return an Optional containing the entity if found, or an empty Optional if no entity is found
     */
    public static Optional<Chat> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Retrieves all entities stored in the repository.
     * @return a list of all entities
     */
    public static List<Chat> findAll() {
        return new ArrayList<>(map.values());
    }
    /**
     * Saves or updates an entity in the repository.
     * If the entity already exists, it is updated in the repository.
     * @param entity the entity to save or update
     * @return the saved or updated entity
     */
    public static Chat save(Chat entity) {
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
     * Finds and returns a list of chats with the specified name.
     *
     * @param name the name of the chats to be searched.
     * @return a list of chats that have the specified name. If no chats with the given name are found,
     * an empty list is returned.
     */
    public static List<Chat> findByName(String name) {
        return map.values().stream().filter(chat -> name.equals(chat.getName())).toList();
    }
}
