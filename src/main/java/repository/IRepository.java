package repository;

import domain.common.BaseEntity;

import java.util.List;

public interface IRepository {

    void save(BaseEntity entity);
    BaseEntity findById(Integer id);
    List<BaseEntity> findAll();
    void delete(BaseEntity entity);


}
