package org.saccharine.grabber.db;

import java.util.List;
import java.util.UUID;

public interface AppDataBase<E> extends AutoCloseable {
    void save(E e);

    List<E> getAll();

    E findById(UUID id);
}