package repository;

import domain.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public interface IRepository<T> {

    void save(T entity);
    T findById(UUID id);
    List<T> findAll();
    void delete(T entity);
}
