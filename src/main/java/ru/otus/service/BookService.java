package ru.otus.service;

import ru.otus.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findByName(String name);
    Optional<Book> findById(long id);
    Book create(String name, String author, String genre);
    Book update(long id, String name, String author, String genre);
    void delete(long id);
}
