package ru.otus.dao;

import ru.otus.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    Optional<Book> findById(long id);
    List<Book> findByName(String name);
    long save(Book book);
    void update(Book book);
    void delete(long id);
}
