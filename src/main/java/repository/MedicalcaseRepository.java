package repository;

import domain.Medicalcase.Medicalcase;

import java.time.Instant;
import java.util.*;

public class MedicalcaseRepository {
    /**
     * A hashmap for storing user entities with their uuid as the key
     */
    private static final HashMap<UUID, Medicalcase> map = new HashMap<>();

    /**
     * Finds an entity by its ID.
     * @param id the ID of the entity to find
     * @return an Optional containing the entity if found, or an empty Optional if no entity is found
     */
    public static Optional<Medicalcase> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    /**
     * Retrieves all entities stored in the repository.
     * @return a list of all entities
     */
    public static List<Medicalcase> findAll() {
        return new ArrayList<>(map.values());
    }
    /**
     * Saves or updates an entity in the repository.
     * If the entity already exists, it is updated in the repository.
     * @param entity the entity to save or update
     * @return the saved or updated entity
     */
    public static Medicalcase save(Medicalcase entity) {
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
     * Finds and returns a list of medicalcases with the specified name.
     *
     * @param name the name of the medicalcases to be searched.
     * @return a list of medicalcases that have the specified name. If no medicalcases with the given name are found,
     * an empty list is returned.
     */
    public static List<Medicalcase> findByName(String name) {
        return map.values().stream().filter(medicalcase -> name.equals(medicalcase.getTitle())).toList();
    }
}
