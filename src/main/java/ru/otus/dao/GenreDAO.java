package ru.otus.dao;

import ru.otus.entity.Genre;

import java.util.Optional;

public interface GenreDAO {
    Optional<Genre> findByName(String name);
}
