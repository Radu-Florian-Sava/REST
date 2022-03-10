package ro.ubbcluj.mpp.proiectproblema1.repository;

import java.util.Collection;

public interface Repository<T, Tid> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    // IMPORTANT: careful, getAll() can be tricky, in a rather sizeable application this method can be problematic
    Collection<T> getAll();

}

