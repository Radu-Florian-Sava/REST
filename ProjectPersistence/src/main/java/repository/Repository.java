package repository;


import model.Identifiable;

import java.util.List;

public interface Repository<T extends Identifiable<Tid>, Tid> {
    void add(T elem);
    void delete(Tid id);
    T update(T elem, Tid id);
    T findById(Tid id);
    // IMPORTANT: careful, getAll() can be tricky, in a rather sizeable application this method can be problematic
    List<T> getAll();

}
