package ro.ubbcluj.mpp.proiectproblema1.repository;

import ro.ubbcluj.mpp.proiectproblema1.model.Identifiable;

import java.util.Collection;

public interface Repository<T extends Identifiable<Tid>, Tid> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    // IMPORTANT: careful, getAll() can be tricky, in a rather sizeable application this method can be problematic
    Collection<T> getAll();

}

