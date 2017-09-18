package it.dturek.github.drivers.service;

import java.util.List;

public interface CrudService<T> {

    T findById(Long id);

    T create(T entity);

    T update(T entity);

    void delete(T entity);

    List<T> findAll();

    void deleteAll();

}
