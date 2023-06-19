package app.persistance;

import app.persistance.utils.RepositoryException;

import java.util.List;

public interface CRUDRepository <ID, T> {

    T get(ID id);
    List<T> getAll();
    T save(T entity) throws RepositoryException;
    T remove(ID id);
    T update(T entity) throws RepositoryException;

}
