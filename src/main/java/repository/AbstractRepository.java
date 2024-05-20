package repository;

import domain.common.BaseEntity;

import java.util.List;
import java.util.UUID;

public abstract class AbstractRepository<T> implements IRepository<T> {

    @Override
    public void save(T entity) {

    }

    @Override
    public T findById(UUID id) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public void delete(T entity) {

    }
}
