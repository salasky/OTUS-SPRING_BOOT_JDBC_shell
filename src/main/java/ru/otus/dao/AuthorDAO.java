package ru.otus.dao;

import ru.otus.entity.Author;

import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> findByName(String name);
}
