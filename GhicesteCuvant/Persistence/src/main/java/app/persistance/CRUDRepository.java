package app.persistance;

import java.util.List;

public interface CRUDRepository <ID, T> {

    T get(ID id);
    List<T> getAll();
    T save(T entity);
    T remove(ID id);
    T update(T entity);

}
