package repository;

import domain.BaseEntity;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface IRepository {

    void save(BaseEntity entity);
    BaseEntity findById(Integer id);
    List<BaseEntity> findAll();
    void delete(BaseEntity entity);


}
